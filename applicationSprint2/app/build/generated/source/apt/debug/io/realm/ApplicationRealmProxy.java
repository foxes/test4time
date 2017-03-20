package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationRealmProxy extends com.foxes.capstone.Application
    implements RealmObjectProxy, ApplicationRealmProxyInterface {

    static final class ApplicationColumnInfo extends ColumnInfo
        implements Cloneable {

        public long nameIndex;
        public long packageNameIndex;
        public long processNameIndex;

        ApplicationColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(3);
            this.nameIndex = getValidColumnIndex(path, table, "Application", "name");
            indicesMap.put("name", this.nameIndex);
            this.packageNameIndex = getValidColumnIndex(path, table, "Application", "packageName");
            indicesMap.put("packageName", this.packageNameIndex);
            this.processNameIndex = getValidColumnIndex(path, table, "Application", "processName");
            indicesMap.put("processName", this.processNameIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final ApplicationColumnInfo otherInfo = (ApplicationColumnInfo) other;
            this.nameIndex = otherInfo.nameIndex;
            this.packageNameIndex = otherInfo.packageNameIndex;
            this.processNameIndex = otherInfo.processNameIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final ApplicationColumnInfo clone() {
            return (ApplicationColumnInfo) super.clone();
        }

    }
    private ApplicationColumnInfo columnInfo;
    private ProxyState<com.foxes.capstone.Application> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("name");
        fieldNames.add("packageName");
        fieldNames.add("processName");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    ApplicationRealmProxy() {
        if (proxyState == null) {
            injectObjectContext();
        }
        proxyState.setConstructionFinished();
    }

    private void injectObjectContext() {
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ApplicationColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.foxes.capstone.Application>(com.foxes.capstone.Application.class, this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @SuppressWarnings("cast")
    public String realmGet$name() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nameIndex);
    }

    public void realmSet$name(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.nameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nameIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$packageName() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.packageNameIndex);
    }

    public void realmSet$packageName(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.packageNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.packageNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.packageNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.packageNameIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$processName() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.processNameIndex);
    }

    public void realmSet$processName(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.processNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.processNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.processNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.processNameIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("Application")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("Application");
            realmObjectSchema.add(new Property("name", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("packageName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("processName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            return realmObjectSchema;
        }
        return realmSchema.get("Application");
    }

    public static Table initTable(SharedRealm sharedRealm) {
        if (!sharedRealm.hasTable("class_Application")) {
            Table table = sharedRealm.getTable("class_Application");
            table.addColumn(RealmFieldType.STRING, "name", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "packageName", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "processName", Table.NULLABLE);
            table.setPrimaryKey("");
            return table;
        }
        return sharedRealm.getTable("class_Application");
    }

    public static ApplicationColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_Application")) {
            Table table = sharedRealm.getTable("class_Application");
            final long columnCount = table.getColumnCount();
            if (columnCount != 3) {
                if (columnCount < 3) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 3 but was " + columnCount);
                }
                if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 3 but was %1$d", columnCount);
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 3 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < columnCount; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final ApplicationColumnInfo columnInfo = new ApplicationColumnInfo(sharedRealm.getPath(), table);

            if (table.hasPrimaryKey()) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key defined for field " + table.getColumnName(table.getPrimaryKey()) + " was removed.");
            }

            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.nameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("packageName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'packageName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("packageName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'packageName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.packageNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'packageName' is required. Either set @Required to field 'packageName' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("processName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'processName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("processName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'processName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.processNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'processName' is required. Either set @Required to field 'processName' or migrate using RealmObjectSchema.setNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'Application' class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Application";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.foxes.capstone.Application createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.foxes.capstone.Application obj = realm.createObjectInternal(com.foxes.capstone.Application.class, true, excludeFields);
        if (json.has("name")) {
            if (json.isNull("name")) {
                ((ApplicationRealmProxyInterface) obj).realmSet$name(null);
            } else {
                ((ApplicationRealmProxyInterface) obj).realmSet$name((String) json.getString("name"));
            }
        }
        if (json.has("packageName")) {
            if (json.isNull("packageName")) {
                ((ApplicationRealmProxyInterface) obj).realmSet$packageName(null);
            } else {
                ((ApplicationRealmProxyInterface) obj).realmSet$packageName((String) json.getString("packageName"));
            }
        }
        if (json.has("processName")) {
            if (json.isNull("processName")) {
                ((ApplicationRealmProxyInterface) obj).realmSet$processName(null);
            } else {
                ((ApplicationRealmProxyInterface) obj).realmSet$processName((String) json.getString("processName"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.foxes.capstone.Application createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.foxes.capstone.Application obj = new com.foxes.capstone.Application();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ApplicationRealmProxyInterface) obj).realmSet$name(null);
                } else {
                    ((ApplicationRealmProxyInterface) obj).realmSet$name((String) reader.nextString());
                }
            } else if (name.equals("packageName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ApplicationRealmProxyInterface) obj).realmSet$packageName(null);
                } else {
                    ((ApplicationRealmProxyInterface) obj).realmSet$packageName((String) reader.nextString());
                }
            } else if (name.equals("processName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ApplicationRealmProxyInterface) obj).realmSet$processName(null);
                } else {
                    ((ApplicationRealmProxyInterface) obj).realmSet$processName((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static com.foxes.capstone.Application copyOrUpdate(Realm realm, com.foxes.capstone.Application object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.foxes.capstone.Application) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.foxes.capstone.Application copy(Realm realm, com.foxes.capstone.Application newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.foxes.capstone.Application) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            com.foxes.capstone.Application realmObject = realm.createObjectInternal(com.foxes.capstone.Application.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((ApplicationRealmProxyInterface) realmObject).realmSet$name(((ApplicationRealmProxyInterface) newObject).realmGet$name());
            ((ApplicationRealmProxyInterface) realmObject).realmSet$packageName(((ApplicationRealmProxyInterface) newObject).realmGet$packageName());
            ((ApplicationRealmProxyInterface) realmObject).realmSet$processName(((ApplicationRealmProxyInterface) newObject).realmGet$processName());
            return realmObject;
        }
    }

    public static long insert(Realm realm, com.foxes.capstone.Application object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.foxes.capstone.Application.class);
        long tableNativePtr = table.getNativeTablePointer();
        ApplicationColumnInfo columnInfo = (ApplicationColumnInfo) realm.schema.getColumnInfo(com.foxes.capstone.Application.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$name = ((ApplicationRealmProxyInterface)object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        }
        String realmGet$packageName = ((ApplicationRealmProxyInterface)object).realmGet$packageName();
        if (realmGet$packageName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.packageNameIndex, rowIndex, realmGet$packageName, false);
        }
        String realmGet$processName = ((ApplicationRealmProxyInterface)object).realmGet$processName();
        if (realmGet$processName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.processNameIndex, rowIndex, realmGet$processName, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.foxes.capstone.Application.class);
        long tableNativePtr = table.getNativeTablePointer();
        ApplicationColumnInfo columnInfo = (ApplicationColumnInfo) realm.schema.getColumnInfo(com.foxes.capstone.Application.class);
        com.foxes.capstone.Application object = null;
        while (objects.hasNext()) {
            object = (com.foxes.capstone.Application) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$name = ((ApplicationRealmProxyInterface)object).realmGet$name();
                if (realmGet$name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                }
                String realmGet$packageName = ((ApplicationRealmProxyInterface)object).realmGet$packageName();
                if (realmGet$packageName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.packageNameIndex, rowIndex, realmGet$packageName, false);
                }
                String realmGet$processName = ((ApplicationRealmProxyInterface)object).realmGet$processName();
                if (realmGet$processName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.processNameIndex, rowIndex, realmGet$processName, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.foxes.capstone.Application object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.foxes.capstone.Application.class);
        long tableNativePtr = table.getNativeTablePointer();
        ApplicationColumnInfo columnInfo = (ApplicationColumnInfo) realm.schema.getColumnInfo(com.foxes.capstone.Application.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$name = ((ApplicationRealmProxyInterface)object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
        }
        String realmGet$packageName = ((ApplicationRealmProxyInterface)object).realmGet$packageName();
        if (realmGet$packageName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.packageNameIndex, rowIndex, realmGet$packageName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.packageNameIndex, rowIndex, false);
        }
        String realmGet$processName = ((ApplicationRealmProxyInterface)object).realmGet$processName();
        if (realmGet$processName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.processNameIndex, rowIndex, realmGet$processName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.processNameIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.foxes.capstone.Application.class);
        long tableNativePtr = table.getNativeTablePointer();
        ApplicationColumnInfo columnInfo = (ApplicationColumnInfo) realm.schema.getColumnInfo(com.foxes.capstone.Application.class);
        com.foxes.capstone.Application object = null;
        while (objects.hasNext()) {
            object = (com.foxes.capstone.Application) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$name = ((ApplicationRealmProxyInterface)object).realmGet$name();
                if (realmGet$name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
                }
                String realmGet$packageName = ((ApplicationRealmProxyInterface)object).realmGet$packageName();
                if (realmGet$packageName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.packageNameIndex, rowIndex, realmGet$packageName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.packageNameIndex, rowIndex, false);
                }
                String realmGet$processName = ((ApplicationRealmProxyInterface)object).realmGet$processName();
                if (realmGet$processName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.processNameIndex, rowIndex, realmGet$processName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.processNameIndex, rowIndex, false);
                }
            }
        }
    }

    public static com.foxes.capstone.Application createDetachedCopy(com.foxes.capstone.Application realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.foxes.capstone.Application unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.foxes.capstone.Application)cachedObject.object;
            } else {
                unmanagedObject = (com.foxes.capstone.Application)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.foxes.capstone.Application();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((ApplicationRealmProxyInterface) unmanagedObject).realmSet$name(((ApplicationRealmProxyInterface) realmObject).realmGet$name());
        ((ApplicationRealmProxyInterface) unmanagedObject).realmSet$packageName(((ApplicationRealmProxyInterface) realmObject).realmGet$packageName());
        ((ApplicationRealmProxyInterface) unmanagedObject).realmSet$processName(((ApplicationRealmProxyInterface) realmObject).realmGet$processName());
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Application = [");
        stringBuilder.append("{name:");
        stringBuilder.append(realmGet$name() != null ? realmGet$name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{packageName:");
        stringBuilder.append(realmGet$packageName() != null ? realmGet$packageName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{processName:");
        stringBuilder.append(realmGet$processName() != null ? realmGet$processName() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationRealmProxy aApplication = (ApplicationRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aApplication.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aApplication.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aApplication.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
