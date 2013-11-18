package playddd.util;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.impl.*;
import org.joda.time.*;

import play.db.jpa.*;
import playddd.models.*;

/**
 * クライテリア検索抽象基底クラス
 */
public class AbstractCriteria<T extends Models> {
    
    protected Integer maxResult = null;
    protected Map<String, OrderBy> orderByMap = Collections.emptyMap();
    
    protected final DetachedCriteria criteria;
    
    //joinTable先のDetachedCriteriaを保持しておく
    protected final Map<String, DetachedCriteria> joinTableMap = new HashMap<String, DetachedCriteria>();
    
    /**
     * コンストラクタ
     * @param clazz 検索対象エンティティクラス
     */
    protected AbstractCriteria(final Class clazz) {
        this.criteria = DetachedCriteria.forClass(clazz);
    }
    
    /**
     * ソート順
     * @param columnName カラム名
     * @param orderBy ソート順
     * */
    protected AbstractCriteria<T> orderBy(final String columnName,
                                          final OrderBy orderBy) {
        if (isEmpty(columnName) || orderBy == null) {
            throw new RuntimeException();
        }
        if (orderByMap.size() == 0) {
            orderByMap = new HashMap<String, AbstractCriteria.OrderBy>();
        }
        orderByMap.put(columnName, orderBy);
        return this;
    }
    
    /** 最大返却数 */
    protected AbstractCriteria<T> maxResult(final Integer maxResult) {
        if (maxResult == null) {
            throw new RuntimeException();
        }
        
        this.maxResult = maxResult;
        return this;
    }
    
    /** 検索実行 */
    protected List<T> search() {
        //条件が未設定であれば空リスト返却
        if (hasFilter() == false) {
            return Collections.<T> emptyList();
        }
        
        //ソートオーダー設定
        if (orderByMap.size() != 0) {
            for (final Map.Entry<String, OrderBy> e : orderByMap.entrySet()) {
                if (e.getValue().equals(OrderBy.ASC)) {
                    criteria.addOrder(Order.asc(e.getKey()));
                }
                else {
                    criteria.addOrder(Order.desc(e.getKey()));
                }
            }
        }
        
        //検索結果返却
        return maxResult != null && maxResult != 0
                ? getExecutableCriteria().setMaxResults(maxResult).list()
                : getExecutableCriteria().list();
    }
    
    /** 検索条件の登録有無 */
    protected boolean hasFilter() {
        final Iterator entries = ((CriteriaImpl) getExecutableCriteria()).iterateExpressionEntries();
        return entries.hasNext();
    }
    
    /** 実行可能クライテリア取得 */
    protected Criteria getExecutableCriteria() {
        return criteria.getExecutableCriteria((Session) JPA.em().getDelegate());
    }
    
    /** equal抽出 */
    protected AbstractCriteria setEqual(final String column, final Object value) {
        if (isEmpty(column) || value == null) {
            throw new RuntimeException();
        }
        
        if (column.indexOf(".") != -1) {
            return setJoinedEqual(toTables(column), toColumn(column), value);
        }
        criteria.add(Restrictions.eq(column, value));
        return this;
    }
    
    /** equal抽出 */
    protected AbstractCriteria setJoinedEqual(final List<String> joinTables,
                                              final String column,
                                              final Object value) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.eq(column, value));
        return this;
    }
    
    /** like抽出 */
    protected AbstractCriteria setLike(final String column, final String value) {
        if (isEmpty(column) || value == null) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedLike(toTables(column), toColumn(column), value);
        }
        criteria.add(Restrictions.ilike(column, "%" + value + "%"));
        return this;
    }
    
    /** like抽出 */
    protected AbstractCriteria setJoinedLike(final List<String> joinTables,
                                             final String column,
                                             final String value) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.ilike(column, "%" + value + "%"));
        return this;
    }
    
    /** in抽出 */
    protected AbstractCriteria setIn(final String column,
                                     final List<String> inList) {
        if (isEmpty(column) || inList == null || inList.size() == 0) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedIn(toTables(column), toColumn(column), inList);
        }
        
        criteria.add(Restrictions.in(column, inList));
        return this;
    }
    
    /** in抽出 */
    protected AbstractCriteria setJoinedIn(final List<String> joinTables,
                                           final String column,
                                           final List<String> inList) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.in(column, inList));
        return this;
    }
    
    /** graterThan抽出 */
    protected AbstractCriteria setGraterThanDate(final String column,
                                                 final DateTime date) {
        if (isEmpty(column) || date == null) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedGraterThanDate(toTables(column),
                                           toColumn(column),
                                           date);
        }
        
        criteria.add(Restrictions.gt(column, date));
        return this;
    }
    
    /** graterThan抽出 */
    protected AbstractCriteria setJoinedGraterThanDate(final List<String> joinTables,
                                                       final String column,
                                                       final DateTime date) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.gt(column, date));
        return this;
    }
    
    /** graterThan or equal抽出 */
    protected AbstractCriteria setGe(final String column, final DateTime date) {
        if (isEmpty(column) || date == null) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedGraterThanOrEqualDate(toTables(column),
                                                  toColumn(column),
                                                  date);
        }
        
        criteria.add(Restrictions.ge(column, date));
        return this;
    }
    
    /** graterThan or equal抽出 */
    protected AbstractCriteria setJoinedGraterThanOrEqualDate(final List<String> joinTables,
                                                              final String column,
                                                              final DateTime date) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.ge(column, date));
        return this;
    }
    
    /** lessThan抽出 */
    protected AbstractCriteria setLessThanDate(final String column,
                                               final DateTime date) {
        if (isEmpty(column) || date == null) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedLessThanDate(toTables(column),
                                         toColumn(column),
                                         date);
        }
        
        criteria.add(Restrictions.lt(column, date));
        return this;
    }
    
    /** lessThan抽出 */
    protected AbstractCriteria setJoinedLessThanDate(final List<String> joinTables,
                                                     final String column,
                                                     final DateTime date) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.lt(column, date));
        return this;
    }
    
    /** lessThan or equal抽出 */
    protected AbstractCriteria setLessThanOrEqualDate(final String column,
                                                      final DateTime date) {
        if (isEmpty(column) || date == null) {
            throw new RuntimeException();
        }
        if (column.indexOf(".") != -1) {
            return setJoinedLessThanOrEqualDate(toTables(column),
                                                toColumn(column),
                                                date);
        }
        
        criteria.add(Restrictions.le(column, date));
        return this;
    }
    
    /** lessThan or equal抽出 */
    protected AbstractCriteria setJoinedLessThanOrEqualDate(final List<String> joinTables,
                                                            final String column,
                                                            final DateTime date) {
        final DetachedCriteria joinCriteria = getJoinCriteria(joinTables);
        joinCriteria.add(Restrictions.le(column, date));
        return this;
    }
    
    /** joinTable用criteriaインスタンス取得 */
    protected DetachedCriteria getJoinCriteria(final List<String> joinTables) {
        DetachedCriteria joinCriteria = joinTableMap.get(joinTables.toString());
        if (joinCriteria == null) {
            joinCriteria = this.criteria;
            for (final String joinTable : joinTables) {
                joinCriteria = joinCriteria.createCriteria(joinTable,
                                                           CriteriaSpecification.LEFT_JOIN);
            }
        }
        joinTableMap.put(joinTables.toString(), joinCriteria);
        return joinCriteria;
    }
    
    /**
     * ピリオド区切りからテーブル名を抽出
     * @param column テーブル名付きカラム名（tableName1.tableName2.columnName）
     * @return テーブル名リスト
     */
    protected List<String> toTables(final String column) {
        final String[] columns = column.split("\\.");
        final List<String> tables = new ArrayList<String>();
        for (final String table : columns) {
            tables.add(table);
            //テーブル部分のみ抽出
            if (tables.size() == columns.length - 1) {
                break;
            }
        }
        return tables;
    }
    
    /**
     * ピリオド区切りから絡む名を抽出
     * @param column テーブル名付きカラム名（tableName1.tableName2.columnName）
     * @return カラム名
     */
    protected String toColumn(final String column) {
        final String[] columns = column.split("\\.");
        return columns[columns.length - 1];
    }
    
    protected static enum OrderBy {
        /** 昇順 */
        ASC,
        /** 降順 */
        DESC;
    }
    
}
