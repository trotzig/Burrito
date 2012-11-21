/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package burrito.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import siena.Id;
import siena.Model;
import siena.Query;
import siena.SienaException;
import burrito.CloneCreator;
import burrito.Configurator;
import burrito.EntityValidationException;
import burrito.annotations.AdminLink;
import burrito.annotations.BBCode;
import burrito.annotations.Cloneable;
import burrito.annotations.DefaultSort;
import burrito.annotations.Displayable;
import burrito.annotations.EmbeddedBy;
import burrito.annotations.FileKey;
import burrito.annotations.Hidden;
import burrito.annotations.Image;
import burrito.annotations.ImageKey;
import burrito.annotations.IndexedByEnum;
import burrito.annotations.Link;
import burrito.annotations.ListedBy;
import burrito.annotations.ListedByEnum;
import burrito.annotations.LongText;
import burrito.annotations.ReadOnly;
import burrito.annotations.RedundantForPerformance;
import burrito.annotations.RegexpValidation;
import burrito.annotations.Relation;
import burrito.annotations.Relation.RenderMode;
import burrito.annotations.Required;
import burrito.annotations.RichText;
import burrito.annotations.SearchableField;
import burrito.annotations.SearchableMethod;
import burrito.annotations.Unique;
import burrito.client.crud.CrudEntityReference;
import burrito.client.crud.CrudGenericException;
import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.CrudPreviewPayload;
import burrito.client.crud.CrudService;
import burrito.client.crud.FieldValueNotUniqueException;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.generic.CrudEntityList;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.AdminLinkMethodField;
import burrito.client.crud.generic.fields.BBCodeField;
import burrito.client.crud.generic.fields.BooleanField;
import burrito.client.crud.generic.fields.DateField;
import burrito.client.crud.generic.fields.DisplayableMethodField;
import burrito.client.crud.generic.fields.EmbeddedListField;
import burrito.client.crud.generic.fields.EnumIndexedListField;
import burrito.client.crud.generic.fields.EnumListField;
import burrito.client.crud.generic.fields.FileField;
import burrito.client.crud.generic.fields.ImageField;
import burrito.client.crud.generic.fields.IntegerField;
import burrito.client.crud.generic.fields.IntegerListField;
import burrito.client.crud.generic.fields.LinkListField;
import burrito.client.crud.generic.fields.LinkedEntityField;
import burrito.client.crud.generic.fields.ListedByEnumField;
import burrito.client.crud.generic.fields.LongField;
import burrito.client.crud.generic.fields.LongListField;
import burrito.client.crud.generic.fields.ManyToManyRelationField;
import burrito.client.crud.generic.fields.ManyToOneRelationField;
import burrito.client.crud.generic.fields.RichTextField;
import burrito.client.crud.generic.fields.StringField;
import burrito.client.crud.generic.fields.StringListField;
import burrito.client.crud.generic.fields.StringSelectionField;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.interfaces.Previewable;
import burrito.links.Linkable;
import burrito.sitelet.Sitelet;
import burrito.util.EntityUtil;
import burrito.util.ValidationUtil;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service that deals with all the serialization/deserialization of objects from
 * and to admin.
 * 
 * Basically: a _lot_ of reflection.
 * 
 * @author henper
 * 
 */
public class CrudServiceImpl extends RemoteServiceServlet implements
		CrudService {

	private static final long serialVersionUID = 1L;

	private SearchManager searchManager = SearchManagerFactory.getSearchManager();
	private PluginCrudManager pluginManager = PluginCrudManager.get();

	@SuppressWarnings("unchecked")
	public List<CrudNameIdPair> getListValues(String entityName) {
		Class<? extends Model> clazz = extractClass(entityName);
		List<Model> all;
		try {
			all = (List<Model>) clazz.getMethod("listValues").invoke(null); // for compatibility with old K9 code
		} catch (Exception e) {
			all = (List<Model>) Model.all(clazz).fetch();
		}
		List<CrudNameIdPair> result = new ArrayList<CrudNameIdPair>(all.size());
		for (Model entity : all) {
			result.add(new CrudNameIdPair(extractIDFromEntity(entity), entity
					.toString()));
		}
		return result;
	}

	public List<String> getEnumListValues(String type) {
		Class<?> clazz;
		try {
			clazz = Class.forName(type);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown enum class: " + type);
		}
		List<String> enums = new ArrayList<String>();
		for (Object o : clazz.getEnumConstants()) {
			enums.add(((Enum<?>) o).name());
		}
		return enums;
	}

	public CrudEntityDescription getEntityHeaders(String entityName) {
		Class<? extends Model> clazz = extractClass(entityName);
		Model fakeEntity = extractEntity(-1l, null, clazz);
		CrudEntityDescription desc = new CrudEntityDescription();
		desc.setCloneable(clazz.isAnnotationPresent(Cloneable.class));
		ArrayList<CrudField> result = new ArrayList<CrudField>();
		ArrayList<CrudField> delayed = new ArrayList<CrudField>();
		//find all Displayable methods
		for (Method method : EntityUtil.getMethods(clazz)) {
			if (method.isAnnotationPresent(SearchableMethod.class)) {
				desc.setSearchable(true);
			}
			Displayable dispAnn = method.getAnnotation(Displayable.class);
			if (dispAnn != null) {
				CrudField crudField = new DisplayableMethodField();
				crudField.setName(method.getName());
				if (dispAnn.last()) {
					//delay it
					delayed.add(crudField);
				} else {
					result.add(crudField);
				}
			}
		}
		//find all Displayable fields
		for (Field field : EntityUtil.getFields(clazz)) {
			if (field.isAnnotationPresent(SearchableField.class)) {
				desc.setSearchable(true);
			}
			Displayable dispAnn = field.getAnnotation(Displayable.class);
			if (dispAnn != null) {
				try {
					CrudField crudField = createCrudField(field, fakeEntity);
					if (dispAnn.last()) {
						delayed.add(crudField);
					} else {
						result.add(crudField);
					}
				} catch (Exception e) {
					throw new RuntimeException("Failed to create crud field", e);
				}
			}
		}
		
		//add all delayed fields
		result.addAll(delayed);
		
		//find all AdminLinks
		for (Method method : EntityUtil.getMethods(clazz)) {
			AdminLink linkAnn = method.getAnnotation(AdminLink.class);
			if (linkAnn != null) {
				CrudField crudField = new AdminLinkMethodField();
				crudField.setName(method.getName());
				result.add(crudField);
			}
		}

		desc.setFields(result);
		desc.setEntityName(entityName);

		return desc;
	}

	public List<CrudEntityInfo> getAllEntities() {
		List<CrudEntityInfo> result = new ArrayList<CrudEntityInfo>();
		synchronized (Configurator.crudables) {
			for (Class<? extends Model> clazz : Configurator.crudables) {
				result.add(new CrudEntityInfo(clazz.getName()));
			}
		}
		return result;
	}

	public Boolean isCrudEnabled(String className) {
		synchronized (Configurator.crudables) {
			for (Class<? extends Model> clazz : Configurator.crudables) {
				if (clazz.getName().equals(className)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public List<String> getLinkableTypes() {
		List<String> result = new ArrayList<String>();
		synchronized (Configurator.linkables) {
			for (Class<? extends Linkable> clazz : Configurator.linkables) {
				result.add(clazz.getName());
			}
		}
		return result;
	}

	public void deleteEntities(List<CrudEntityDescription> selected) {
		for (CrudEntityDescription crud : selected) {
			Class<? extends Model> clazz = extractClass(crud.getEntityName());
			Long entityId = crud.getId();
			Model entity = extractEntity(entityId, null, clazz);
			cascadeDeleteRelations(entity);
			entity.delete();
		}
	}

	private void cascadeDeleteRelations(Model toBeDeleted) {
		// Searches model entities to find relations to the object about to be
		// deleted
		Long toBeDeletedId = extractIDFromEntity(toBeDeleted);
		synchronized (Configurator.crudables) {

			for (Class<? extends Model> clazz : Configurator.crudables) {
				for (Field field : EntityUtil.getFields(clazz)) {
					if (field.isAnnotationPresent(Relation.class)) {
						Relation relation = field.getAnnotation(Relation.class);
						boolean isCorrectClass = relation.value().equals(toBeDeleted.getClass());
						boolean shouldRemoveDeadReferences = relation.allowDeadReference();
						if (isCorrectClass && !shouldRemoveDeadReferences) {
							// Get relateted class values based on the id from
							// the
							// deleted entity
							List<? extends Model> relateds = Model.all(clazz)
									.filter(field.getName(), toBeDeletedId)
									.fetch();
							for (Model related : relateds) {
								try {
									if (field.getType() == Long.class) {
										field.setAccessible(true);
										field.set(related, null);
										field.setAccessible(false);
										related.update();
									} else if (field.getType() == List.class) {
										@SuppressWarnings("unchecked")
										List<Long> list = (List<Long>) field
												.get(related);
										if (list != null) {
											list.remove(toBeDeletedId);
											related.update();
										}
									}
								} catch (Exception e) {
									throw new RuntimeException(
											"Failed to update related entity. This could mean that the database is inconsistent. ",
											e);
								}
							}
						}
					}
				}

			}
		}

	}

	public CrudEntityList listEntities(String filter, String entityName,
			PageMetaData<String> p) {
		Class<? extends Model> clazz = extractClass(entityName);

		if (filter != null) {
			return search(clazz, filter, p);
		}

		// Prepare query object
		@SuppressWarnings("unchecked")
		Query<Model> q = (Query<Model>) Model.all(clazz);

		// Get a list of entities from the current page:
		String order = null;
		if (p.getSortKey() != null) {
			order = p.getSortKey();
			if (!p.isAscending()) {
				order = "-" + order;
			}
		}
		if (order != null) {
			q.order(order);
		}
		// If a filter is available, use it!
		// if (filter != null) {
		// // Using a technique found here:
		// //
		// http://code.google.com/appengine/docs/python/datastore/queriesandindexes.html#Introducing_Indexes
		// q.filter(filter.getName() + " >=", filter.getValue());
		// q.filter(filter.getName() + " <", filter.getValue() + "\ufffd");
		// }
		List<Model> entities = q.fetch(p.getItemsPerPage(),
				(int) p.getRangeStart());

		// Check if there is a next page by fetching the first entity from the
		// next page:
		boolean hasNextPage = Model.all(clazz).fetch(1, (int) p.getRangeEnd())
				.size() == 1;
		CrudEntityList collection = new CrudEntityList();
		collection.setItems(convertEntitesToCrudEntityDescriptions(entities));
		collection.setPage(p.getPage());
		collection.setHasNextPage(hasNextPage);
		collection.setItemsPerPage(p.getItemsPerPage());
		return collection;
	}

	
	private CrudEntityList search(Class<? extends Model> clazz, String filter,
			PageMetaData<String> p) {
		
		ItemCollection<SearchHit> entries;
		entries = searchManager.search(clazz, filter, p);
				
		List<Model> entities = new ArrayList<Model>();
		for (SearchHit entry : entries) {
			Model entity = extractEntity(entry.getOwnerId(), null, clazz);
			if (entity != null)
				entities.add(entity);
		}
		
		if (Configurator.SEARCH_MANAGER_TYPE == DatastoreSearchManager.class) {
			//Must sort results after search, since this search manager doesn't know how to page or order results.
			sortTable(entities, p);
		}
		
		
		CrudEntityList collection = new CrudEntityList();
		collection.setItems(convertEntitesToCrudEntityDescriptions(entities));
		collection.setPage(p.getPage());
		collection.setHasNextPage(entries.isHasNextPage());
		collection.setItemsPerPage(p.getItemsPerPage());
		return collection;
	}

	private List<CrudEntityDescription> convertEntitesToCrudEntityDescriptions(
			List<Model> entities) {
		List<CrudEntityDescription> result = new ArrayList<CrudEntityDescription>();
		for (Model entity : entities) {
			result.add(createEntityDescription(entity.getClass().getName(),
					extractIDFromEntity(entity), entity.getClass(), entity));
		}
		return result;
	}

	private Long extractIDFromEntity(Model entity) {
		try {
			Field id = EntityUtil.getField(entity.getClass(), "id");
			id.setAccessible(true);
			return (Long) id.get(entity);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get id field from entity", e);
		}
	}
	

	private void sortTable(List<Model> entities, PageMetaData<String> p){
		if(p.isAscending()){
			Collections.sort(entities, new EntityComparator(p.getSortKey()));
		}else{
			Collections.sort(entities, Collections.reverseOrder(new EntityComparator(p.getSortKey())));
		}
	}
	
	public Long save(CrudEntityDescription desc, Long clonedFromId) throws FieldValueNotUniqueException, CrudGenericException {
		Class<? extends Model> clazz = extractClass(desc.getEntityName());
		Model entity = extractEntity(desc.getId(), clonedFromId, clazz);
		updateEntityFromDescription(entity, desc, clazz);
		validateEntityUniqueness(entity, desc, clazz);

		try {
			if (desc.isNew()) {
				entity.insert();
			} else {
				entity.update();
				if (entity instanceof Sitelet) {
					SiteletProperties prop = SiteletProperties.getByEntityId(desc.getId());
					prop.triggerRefreshAsync();
				}
			}
	
			Long id = extractIDFromEntity(entity);
	
			return id;
		} catch (SienaException e) {
			EntityValidationException ve = ValidationUtil.getValidationErrorOrRethrow(e);
			throw new CrudGenericException(ve.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateEntityFromDescription(Object entity,
			CrudEntityDescription desc, Class<?> clazz) {
		for (CrudField field : desc.getFields()) {
			try {
				// Should we really overwrite values from readonly fields?
				// Perhaps skip field if field.isReadOnly()?
				Object value = field.getValue();
				if (field instanceof EmbeddedListField) {
					value = deserializeEmbedded(
							((EmbeddedListField) field).getEmbeddedClassName(),
							(List<CrudEntityDescription>) field.getValue());
				}
				
				Field privField = EntityUtil.getField(clazz, field.getName());
				Class fieldType = privField.getType();
				
				if (value != null && field instanceof ListedByEnumField && (Enum.class.isAssignableFrom(fieldType))) {
					ListedByEnumField fieldEnum = (ListedByEnumField) field;
					String className = fieldEnum.getTypeClassName();
					Class enumClass = Class.forName(className);
					
					value = Enum.valueOf(enumClass, (String) value);
				}
				
				privField.setAccessible(true);
				privField.set(entity, value);

			} catch (Exception e) {
				throw new RuntimeException("Failed to set field "
						+ field.getName(), e);
			}
		}
	}


	private void validateEntityUniqueness(Model entity, CrudEntityDescription desc, Class<?> clazz) throws FieldValueNotUniqueException {
		Object id;

		try {
			Field field = EntityUtil.getField(clazz, "id");
			field.setAccessible(true);
			id = field.get(entity);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to get entity ID", e);
		}

		for (CrudField field : desc.getFields()) {
			String fieldName = field.getName();
			try {
				Field privField = EntityUtil.getField(clazz, fieldName);
				if (privField.isAnnotationPresent(Unique.class)) {
					privField.setAccessible(true);
					Object value = privField.get(entity);
					if (value != null) {
						Field idField = EntityUtil.getField(entity.getClass(), "id");
						idField.setAccessible(true);
						for (Model existing : Model.all(entity.getClass()).filter(fieldName, value).fetch()) {
							if (!idField.get(existing).equals(id)) {
								throw new FieldValueNotUniqueException(fieldName);
							}
						}
					}
				}
			}
			catch (FieldValueNotUniqueException e) {
				throw e; // pass it on
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to validate uniqueness for field " + field.getName(), e);
			}
		}
	}

	private List<String> deserializeEmbedded(String embeddedClassName,
			List<CrudEntityDescription> value) {
		List<String> result = new ArrayList<String>();
		for (CrudEntityDescription desc : value) {
			Class<?> clazz;
			try {
				clazz = Class.forName(embeddedClassName);
				Object entity = clazz.newInstance();
				updateEntityFromDescription(entity, desc, clazz);
				Gson gson = new Gson();
				String json = gson.toJson(entity, clazz);
				result.add(json);
			} catch (Exception e) {
				throw new RuntimeException("Failed to instantiate class: "
						+ embeddedClassName, e);
			}
		}
		return result;
	}

	public CrudEntityDescription describe(String entityName, Long id,
			Long copyFromId) {
		Class<? extends Model> clazz = extractClass(entityName);
		Object entity = extractEntity(id, copyFromId, clazz);

		CrudEntityDescription desc = createEntityDescription(entityName, id, clazz, entity);
		desc.setClonedFromId(copyFromId);
		return desc;
	}

	public CrudEntityDescription describeEmbeddedObject(String embeddedClassName) {
		try {
			Class<?> clazz = Class.forName(embeddedClassName);
			Object o = clazz.newInstance();
			return createEntityDescription(embeddedClassName, null, clazz, o);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("No such class: " + embeddedClassName, e);
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Failed to create embedded object of type "
							+ embeddedClassName, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Failed to create embedded object of type "
							+ embeddedClassName, e);
		}

	}

	private CrudEntityDescription createEntityDescription(String entityName,
			Long id, Class<?> clazz, Object entity) {
		CrudEntityDescription desc = new CrudEntityDescription();
		desc.setCloneable(clazz.isAnnotationPresent((Class<? extends Annotation>) Cloneable.class));
		desc.setEntityName(entityName);
		desc.setId(id);
		desc.setDisplayString(entity.toString());
		
		for (Method method : EntityUtil.getMethods(clazz)) {
			Displayable displayable = method.getAnnotation(Displayable.class);
			if (displayable != null) {
				CrudField cf = new DisplayableMethodField();
				cf.setIconUrlOnTrue(displayable.iconUrlOnTrue());
				cf.setUseAsIconUrl(displayable.useAsIconUrl());
				cf.setName(method.getName());
				try {
					cf.setValue(method.invoke(entity));
				} catch (Exception e) {
					throw new RuntimeException("Failed to invoke method", e);
				}
				desc.add(cf);
			}
		}
		List<Field> fields = EntityUtil.getFields(clazz);
		for (Field field : fields) {
			if (!okField(field)) {
				// skip the id field
				continue;
			}
			field.setAccessible(true);
			CrudField crudField;
			try {
				crudField = createCrudField(field, entity);
			} catch (Exception e) {
				throw new RuntimeException("Failed to create CrudField", e);
			}
			desc.add(crudField);
		}
		// Find AdminLinks
		for (Method method : EntityUtil.getMethods(clazz)) {
			AdminLink linkAnn = method.getAnnotation(AdminLink.class);
			if (linkAnn != null) {
				AdminLinkMethodField cf = new AdminLinkMethodField();
				cf.setName(method.getName());
				cf.setText(linkAnn.text());
				try {
					cf.setValue(method.invoke(entity));
				} catch (Exception e) {
					throw new RuntimeException("Failed to invoke method", e);
				}
				desc.add(cf);
			}
		}

		//scan through interfaces
		for (Class<?> interfaze : EntityUtil.getInterfaces(clazz)) {
			if (interfaze.equals(Previewable.class)) {
				desc.setPreviewable(true);
			}
		}

		return desc;
	}

	private boolean okField(Field field) {
		if ((field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT) {
			// skip transient fields
			return false;
		}
		if (field.isAnnotationPresent(Id.class)) {
			// skip the id field
			return false;
		}
		if (field.getType().equals(Class.class)) {
			return false;
		}
		if (Modifier.isStatic(field.getModifiers())) {
			return false;
		}
		if (field.isAnnotationPresent(Hidden.class)) {
			return false;
		}
		if (field.isAnnotationPresent(RedundantForPerformance.class)) {
			return false;
		}
		return true;
	}

	private CrudField createCrudField(Field field, Object entity)
			throws Exception {
		field.setAccessible(true);

		CrudField crud = pluginManager.process(field, entity); 
		if (crud == null) {
			crud = processStandardCrud(field, entity);
		}
		crud.setName(field.getName());
		crud.setRequired(field.isAnnotationPresent(Required.class));
		
		boolean searchable = field.isAnnotationPresent(SearchableField.class) || field.isAnnotationPresent(SearchableMethod.class);
		crud.setSearchable(searchable);
		
		if (field.isAnnotationPresent(DefaultSort.class)) {
			crud.setDefaultSort(true);
			crud.setSortAscending(field.getAnnotation(DefaultSort.class)
					.ascending());
		}
		Displayable displayable = field.getAnnotation(Displayable.class);
		if (displayable != null) {
			crud.setIconUrlOnTrue(displayable.iconUrlOnTrue());
			crud.setUseAsIconUrl(displayable.useAsIconUrl());
		}

		return crud;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CrudField processStandardCrud(Field field, Object entity)
			throws IllegalAccessException {
		CrudField crud = null;
		
		Class clazz = field.getType();
		
		if (Enum.class.isAssignableFrom(clazz)) {
			Enum value = (Enum) field.get(entity);
			String clazzName = clazz.getName();
			
			crud = new ListedByEnumField((value == null) ? null : value.name(), clazzName);
		
		} else if (clazz == Date.class) {
			crud = new DateField((Date) field.get(entity),
					field.isAnnotationPresent(ReadOnly.class));
			
		} else if (clazz == Boolean.class) {
			crud = new BooleanField((Boolean) field.get(entity));
			
		} else if (clazz == Long.class) {
			if (field.isAnnotationPresent(Relation.class)) {
				Relation annotation = field.getAnnotation(Relation.class);
				
				Class<? extends Model> model = annotation.value();
				String modelName = model.getName();
				
				RenderMode renderMode = annotation.renderMode();
				String modeValue = renderMode.getValue();
				
				String searchSortField = annotation.searchSortField();
				
				crud = new ManyToOneRelationField((Long) field.get(entity), modelName, modeValue, searchSortField);
			} else {
				crud = new LongField((Long) field.get(entity));
			}
			
		} else if (clazz == Integer.class) {
			crud = new IntegerField((Integer) field.get(entity));
			
		} else if (clazz == List.class) {
			ParameterizedType pType = (ParameterizedType) field
					.getGenericType();
			Class type = (Class) pType.getActualTypeArguments()[0];
			
			if (field.isAnnotationPresent(EmbeddedBy.class)) {
				EmbeddedBy embeddedBy = field.getAnnotation(EmbeddedBy.class);
				crud = createEmbeddedListField(embeddedBy.value(),
						(List<String>) field.get(entity));
				
			} else if (type.equals(Long.class) && field.isAnnotationPresent(Relation.class)) {
				String relatedEntityClass = field.getAnnotation(Relation.class)
						.value().getName();
				crud = new ManyToManyRelationField(
						(List<Long>) field.get(entity), relatedEntityClass);
				
			} else if (type.equals(String.class) && field.isAnnotationPresent(Link.class)) {
				crud = new LinkListField((List<String>) field.get(entity));
				
			} else if (field.isAnnotationPresent(ListedByEnum.class)) {
				ListedByEnum annot = field.getAnnotation(ListedByEnum.class);
				crud = new EnumListField((List<String>) field.get(entity), annot.type().getName());
				
			} else if (field.isAnnotationPresent(IndexedByEnum.class)) {
				IndexedByEnum annot = field.getAnnotation(IndexedByEnum.class);
				
				Type genericType = field.getGenericType();
				String genericTypeString = genericType.toString();
				
				if (genericTypeString.equals("java.util.List<java.lang.String>")) {
					crud = new EnumIndexedListField((List<Object>) field.get(entity), annot.type().getName(), String.class.getName());
					
				} else if (genericTypeString.equals("java.util.List<java.util.Date>")) {
					crud = new EnumIndexedListField((List<Object>) field.get(entity), annot.type().getName(), Date.class.getName());
				}
				
			} else if (type.equals(String.class)) {
				crud = new StringListField((List<String>) field.get(entity));
				
			} else if (type.equals(Integer.class)) {
				crud = new IntegerListField((List<Integer>) field.get(entity));
				
			} else if (type.equals(Long.class)) {
				crud = new LongListField((List<Long>) field.get(entity));
				
			} else {
				throw new RuntimeException("Unknown list type: " + type);
			}
			
		} else if (clazz == String.class && field.isAnnotationPresent(ListedByEnum.class)) {
			ListedByEnum lenum = field.getAnnotation(ListedByEnum.class);
			crud = new ListedByEnumField((String) field.get(entity), lenum.type().getName());
			
		} else if (clazz == String.class && field.isAnnotationPresent(Image.class)) {
			Image image = field.getAnnotation(Image.class);
			crud = new ImageField((String) field.get(entity), image.width(), image.height(), true);
			
		} else if (clazz == String.class && field.isAnnotationPresent(ImageKey.class)) {
			crud = new ImageField((String) field.get(entity), 0, 0, false);
			
		} else if (clazz == String.class && field.isAnnotationPresent(FileKey.class)) {
			crud = new FileField((String) field.get(entity));	
			
		} else if (clazz == String.class && field.isAnnotationPresent(RichText.class)) {
			crud = new RichTextField((String) field.get(entity));
		
		} else if (clazz == String.class && field.isAnnotationPresent(BBCode.class)) {
			crud = new BBCodeField((String) field.get(entity));	
			
		} else if (clazz == String.class && field.isAnnotationPresent(ListedBy.class)) {
			ListedBy listedBy = field.getAnnotation(ListedBy.class);
			String[] list = listedBy.value();
			crud = new StringSelectionField((String) field.get(entity), list);
			
		} else if (clazz == String.class && field.isAnnotationPresent(Link.class)) {
			crud = new LinkedEntityField((String) field.get(entity));
			
		} else if (clazz == String.class) {
			StringField stringCrud = new StringField((String) field.get(entity));
			if (field.isAnnotationPresent(RegexpValidation.class)) {
				RegexpValidation regexp = field
						.getAnnotation(RegexpValidation.class);
				stringCrud.setRegexpPattern(regexp.pattern());
				stringCrud.setRegexpDescription(regexp.description());
			}
			if (field.isAnnotationPresent(LongText.class)) {
				stringCrud.setRenderAsTextArea(true);
			}
			crud = stringCrud;
			
		} else {
			throw new RuntimeException("No such field type: " + clazz.getName());
		}
		
		if (field.isAnnotationPresent(ReadOnly.class)) {
			crud.setReadOnly(true);
		}
		
		return crud;
	}

	private CrudField createEmbeddedListField(Class<?> type,
			List<String> jsonList) {
		List<CrudEntityDescription> descs = new ArrayList<CrudEntityDescription>();
		if (jsonList != null) {
			for (String embedded : jsonList) {
				Gson gson = new Gson();
				Object overlay = gson.fromJson(embedded, type);
				CrudEntityDescription desc = createEntityDescription(
						type.getName(), null, type, overlay);
				descs.add(desc);
			}
		}
		@SuppressWarnings("unchecked")
		EmbeddedListField field = new EmbeddedListField(descs,
				((Class<? extends Model>) type).getName());
		return field;
	}

	private Model extractEntity(Long id, Long copyFromId, Class<? extends Model> clazz) {
		// Based on the type and database id, fetches an entity from the
		// database. Id -1 is treated specially and is used to signal the
		// creation of a new object.
		Model entity = null;
		if (id == -1) {
			// Id -1 means get a description for a new object of the type
			if (copyFromId != null) {
				// the field values are to be copied from another object:
				entity = (Model) Model.all(clazz).filter("id", copyFromId)
						.get();
				if (Arrays.asList(clazz.getInterfaces()).contains(CloneCreator.class)) {
					entity = ((CloneCreator<?>)entity).createClone();
				}
				resetId(entity);
			} else {
				entity = createNewEntity(clazz);
			}
		} else {
			entity = (Model) Model.all(clazz).filter("id", id).get();
		}
		return entity;
	}

	private Model createNewEntity(Class<? extends Model> clazz) {
		try {
			return (Model) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to construct an object of type " + clazz.getName(), e);
		}
	}

	private void resetId(Model entity) {
		// set the id field to null
		try {
			Field idField = EntityUtil.getField(entity.getClass(), "id");
			idField.setAccessible(true);
			idField.set(entity, null);
			idField.setAccessible(false);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set id to null", e);
		}
	}

	private Class<? extends Model> extractClass(String entityName) {
		return EntityUtil.getClazz(entityName); 
	}

	@Override
	public List<CrudEntityReference> getDisplayNames(
			Set<CrudEntityReference> references) {
		List<CrudEntityReference> result = new ArrayList<CrudEntityReference>();
		for (CrudEntityReference ref : references) {
			Class<? extends Model> clazz = extractClass(ref.getEntityName());
			Model fromDatabase = Model.all(clazz).filter("id", ref.getId()).get();
			ref.setDisplayString(fromDatabase.toString());
			result.add(ref);
		}
		return result;
	}

	@Override
	public CrudPreviewPayload getPreviewPayload(CrudEntityDescription desc) {
		Class<? extends Model> clazz = extractClass(desc.getEntityName());
		Model entity = createNewEntity(clazz);
		updateEntityFromDescription(entity, desc, clazz);

		Previewable previewable = (Previewable) entity;
		return new CrudPreviewPayload(previewable.getPreviewData(), previewable.getPreviewUrl());
	}
	
	
	@Override
	public Integer count(String entityClassName) {
		return Model.all(extractClass(entityClassName)).count();
	}
	
	@Override
	public void reindex(String entityClassName, PageMetaData<String> page) {
		@SuppressWarnings("unchecked")
		List<Model> entities = (List<Model>) Model.all(extractClass(entityClassName)).order("id").fetch(page.getItemsPerPage(), (int)page.getRangeStart());
		for (Model model : entities) {
			searchManager.insertOrUpdateSearchEntry(model, extractIDFromEntity(model));
		}
	}
	
	@Override
	public boolean clearIndexForEntity(String entityName) {
		return searchManager.clearIndexForEntity(extractClass(entityName));
	}

	@Override
	public void reindexPartial(String entityClassName) {
		
		Class<? extends Model> clazz = extractClass(entityClassName);
		Date lastModified = searchManager.getLastModified(clazz);
		if (lastModified == null) {
			throw new RuntimeException("Failed to get last modified date of entity in search index. Run a full reindex instead.");
		}
		@SuppressWarnings("unchecked")
		List<Model> entities = (List<Model>) Model.all(clazz).filter("lastModified>", lastModified).fetch();
		for (Model model : entities) {
			searchManager.insertOrUpdateSearchEntry(model, extractIDFromEntity(model));
		}
	}
	
}
