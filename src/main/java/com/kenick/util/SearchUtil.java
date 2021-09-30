package com.kenick.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/9/27
 */
public class SearchUtil {
    private final static Logger logger = LoggerFactory.getLogger(SearchUtil.class);

    public final static String OPERATOR_GTE = ">=";
    public final static String OPERATOR_LTE = "<=";
    public final static String OPERATOR_NET = "!=";
    public final static String OPERATOR_GT = ">";
    public final static String OPERATOR_ET = "=";
    public final static String OPERATOR_LT = "<";
    public final static String OPERATOR_AND = "AND";
    public final static String OPERATOR_OR = "OR";
    private final static List<String> OPERATOR_LIST = new ArrayList<>(Arrays.asList(OPERATOR_GTE,OPERATOR_LTE,OPERATOR_NET,OPERATOR_GT,OPERATOR_ET,OPERATOR_LT));

    /**
     * <一句话功能简述> 生成通用es查询
     * <功能详细描述>
     *{
     * 	"dataAmount": "10",
     * 	"andFilter": "display_start_date<=1632624039672,(display_end_date=0 OR display_end_date>=1632624039672),",
     * 	"searchParam": "default:'6018' AND is_active:'2' AND item_type:'2' AND item_state:'6'",
     * 	"dataStart": "0",
     * 	"sort": "pay_amount:DECREASE;shelves_date:DECREASE"
     * }
     * author: zhanggw
     * 创建时间:  2021/9/27
     */
    public static SearchSourceBuilder generateEsQuery(JSONObject searchTermJson){
        try{
            if(searchTermJson == null){
                return null;
            }

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 总查询条件
            BoolQueryBuilder topQuery = QueryBuilders.boolQuery();

            // 模糊搜索添加参数
            matchAddSearchParam(topQuery, searchTermJson.getString("searchParam"));

            // 精确搜索添加参数
            termAddSearchParam(topQuery, searchTermJson.getString("termParam"));

            // 添加过滤器
            queryAddFilter(topQuery, searchTermJson.getString("andFilter"));

            sourceBuilder.query(topQuery);

            Integer dataStartInteger = searchTermJson.getInteger("dataStart");
            Integer dataAmountInteger = searchTermJson.getInteger("dataAmount");

            if(dataStartInteger != null){
                sourceBuilder.from(dataStartInteger);
            }
            if(dataAmountInteger != null){
                sourceBuilder.size(dataAmountInteger);
            }

            // 添加排序
            queryAddSort(sourceBuilder, searchTermJson.getString("sort"));

            return sourceBuilder;
        }catch (Exception e){
            logger.error("生成通用es查询异常!", e);
        }
        return null;
    }

    /**
     * <一句话功能简述> 在BoolQueryBuilder中添加模糊匹配
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param searchParamStr 模糊搜索参数  (platform_type:'1' OR platform_type:'3') AND default:'5473' AND is_active:'2' AND item_type:'2' AND (item_state:'6' OR item_state:'7')
     */
    private static void matchAddSearchParam(BoolQueryBuilder topQuery, String searchParamStr) {
        try{
            if(topQuery == null || StringUtils.isBlank(searchParamStr)){
                return;
            }

            logger.debug("BoolQueryBuilder开始添加模糊搜索参数,searchParam:{}", searchParamStr);
            String[] searchParamArray = searchParamStr.split(OPERATOR_AND);
            if(searchParamArray.length > 0){
                for(String searchParam:searchParamArray){ // 第一层and filter
                    searchParam = searchParam.trim();
                    if(StringUtils.isBlank(searchParam)){
                        continue;
                    }

                    if(searchParam.contains("OR")){ // 搜索带or
                        searchParam = searchParam.replace("(","").replace(")","");
                        String[] orMatchArray = searchParam.split(OPERATOR_OR);
                        if(orMatchArray.length > 0){
                            logger.trace("or match,searchParam:{}", searchParam);
                            BoolQueryBuilder orMatchQuery = QueryBuilders.boolQuery();
                            for(String orSubMatch:orMatchArray){
                                QueryBuilder matchQuery = getMatchQuery(orSubMatch);
                                if(matchQuery != null){
                                    orMatchQuery.should(matchQuery);
                                }
                            }
                            topQuery.must(orMatchQuery);
                        }
                    }else{
                        QueryBuilder matchQuery = getMatchQuery(searchParam);
                        if(matchQuery != null){
                            topQuery.must(matchQuery);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("topQuery添加过滤条件异常!", e);
        }
    }

    /**
     * <一句话功能简述> 在BoolQueryBuilder中添加精确匹配
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param termParamStr 精确搜索参数  (platform_type:'1' OR platform_type:'3') AND default:'5473' AND is_active:'2' AND item_type:'2' AND (item_state:'6' OR item_state:'7')
     */
    private static void termAddSearchParam(BoolQueryBuilder topQuery, String termParamStr) {
        try{
            if(topQuery == null || StringUtils.isBlank(termParamStr)){
                return;
            }

            logger.debug("BoolQueryBuilder开始添加精确搜索参数,termParamStr:{}", termParamStr);
            String[] termParamArray = termParamStr.split(OPERATOR_AND);
            if(termParamArray.length > 0){
                for(String termParam:termParamArray){ // 第一层and filter
                    termParam = termParam.trim();
                    if(StringUtils.isBlank(termParam)){
                        continue;
                    }

                    if(termParam.contains("OR")){ // 搜索带or
                        termParam = termParam.replace("(","").replace(")","");
                        String[] orTermArray = termParam.split(OPERATOR_OR);
                        if(orTermArray.length > 0){
                            logger.debug("or term,termParam:{}", termParam);
                            BoolQueryBuilder orTermQuery = QueryBuilders.boolQuery();
                            for(String orSubTerm:orTermArray){
                                QueryBuilder termQuery = getTermQuery(orSubTerm);
                                if(termQuery != null){
                                    orTermQuery.should(termQuery);
                                }
                            }
                            topQuery.must(orTermQuery);
                        }
                    }else{
                        QueryBuilder termQuery = getTermQuery(termParam);
                        if(termQuery != null){
                            topQuery.must(termQuery);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("topQuery添加过滤条件异常!", e);
        }
    }

    /**
     * <一句话功能简述> 在BoolQueryBuilder中添加过滤条件
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param andFilterStr "display_start_date<=1632624039672,(display_end_date='' OR display_end_date>=1632624039672),",
     */
    private static void queryAddFilter(BoolQueryBuilder topQuery, String andFilterStr) {
        try{
            if(topQuery == null || StringUtils.isBlank(andFilterStr)){
                return;
            }

            logger.debug("BoolQueryBuilder开始添加过滤条件,andFilterStr:{}", andFilterStr);
            String[] filterConditionArray = andFilterStr.split(",");
            if(filterConditionArray.length > 0){
                for(String filterCondition:filterConditionArray){ // 第一层and filter
                    filterCondition = filterCondition.trim();
                    if(StringUtils.isBlank(filterCondition)){
                        continue;
                    }
                    filterCondition = filterCondition.replace("or", OPERATOR_OR);

                    if(filterCondition.contains(OPERATOR_OR)){ // 过滤中带or
                        filterCondition = filterCondition.replace("(","").replace(")","");
                        String[] orFilterArray = filterCondition.split(OPERATOR_OR);
                        if(orFilterArray.length > 0){
                            BoolQueryBuilder orFilterQuery = QueryBuilders.boolQuery();
                            for(String orSubFilter:orFilterArray){
                                orFilterQuery.should(getFilterQuery(orSubFilter));
                            }
                            topQuery.filter(orFilterQuery);
                        }
                    }else{
                        topQuery.filter(getFilterQuery(filterCondition));
                    }
                }
            }
        }catch (Exception e){
            logger.error("topQuery添加过滤条件异常!", e);
        }
    }

    /**
     * <一句话功能简述> 过滤基本语句转化
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param filterParamBasic 过滤基本语句 display_start_date<=1632624039672
     */
    private static QueryBuilder getFilterQuery(String filterParamBasic) {
        QueryBuilder queryBuilder = null;
        for(String operator:OPERATOR_LIST){
            if(filterParamBasic.contains(operator)){
                String[] filterArray = filterParamBasic.split(operator);
                String filterName = camelCaseName(filterArray[0]);
                String filterValue = filterArray[1];
                filterValue = filterValue.replace("'","");
                logger.debug("filterParamBasic:{},filterName:{},filterValue:{}", filterParamBasic,filterName, filterValue);
                switch (operator){
                    case SearchUtil.OPERATOR_GTE:
                        queryBuilder = QueryBuilders.rangeQuery(filterName).gte(filterValue);
                        break;
                    case SearchUtil.OPERATOR_NET:
                        if(StringUtils.isBlank(filterValue)){
                            queryBuilder = QueryBuilders.existsQuery(filterName);
                        }else{
                            queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(filterName, filterValue));
                        }
                        break;
                    case SearchUtil.OPERATOR_LTE:
                        queryBuilder = QueryBuilders.rangeQuery(filterName).lte(filterValue);
                        break;
                    case SearchUtil.OPERATOR_GT:
                        queryBuilder = QueryBuilders.rangeQuery(filterName).gt(filterValue);
                        break;
                    case SearchUtil.OPERATOR_ET:
                        if(StringUtils.isBlank(filterValue)){
                            queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(filterName));
                        }else{
                            queryBuilder = QueryBuilders.termQuery(filterName, filterValue);
                        }
                        break;
                    case SearchUtil.OPERATOR_LT:
                        queryBuilder = QueryBuilders.rangeQuery(filterName).lt(filterValue);
                        break;
                }
                break;
            }
        }

        return queryBuilder;
    }

    /**
     * <一句话功能简述> 模糊搜索参数转化
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param searchParamBasic 模糊搜索参数 is_active:'2'
     */
    private static QueryBuilder getMatchQuery(String searchParamBasic) {
        try{
            if(StringUtils.isBlank(searchParamBasic)){
                return null;
            }
            int separateIndex = searchParamBasic.indexOf(":");
            String fieldName = camelCaseName(searchParamBasic.substring(0, separateIndex));
            String fieldValue = searchParamBasic.substring(separateIndex);
            fieldValue = fieldValue.replace("'","").replace(":","");
            logger.debug("searchParamBasic:{},fieldName:{},fieldValue:{}", searchParamBasic, fieldName, fieldValue);
            return QueryBuilders.matchQuery(fieldName, fieldValue);
        }catch (Exception e){
            logger.error("getMatchQuery error!", e);
        }
        return null;
    }

    /**
     * <一句话功能简述> 精确搜索参数转化
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param termParamBasic 模糊搜索参数 is_active:'2'
     */
    private static QueryBuilder getTermQuery(String termParamBasic) {
        try{
            if(StringUtils.isBlank(termParamBasic)){
                return null;
            }
            int separateIndex = termParamBasic.indexOf(":");
            String fieldName = camelCaseName(termParamBasic.substring(0, separateIndex));
            String fieldValue = termParamBasic.substring(separateIndex);
            fieldValue = fieldValue.replace("'","").replace(":","");
            logger.debug("termParamBasic:{},fieldName:{},fieldValue:{}", termParamBasic, fieldName, fieldValue);
            return QueryBuilders.termQuery(fieldName, fieldValue);
        }catch (Exception e){
            logger.error("getTermQuery error!", e);
        }
        return null;
    }

    /**
     * <一句话功能简述> 含下划线字符串转换为驼峰式字符串
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/9/27
     */
    public static String camelCaseName(String underscoreName) {
        if(StringUtils.isBlank(underscoreName) || !underscoreName.contains("_")){
            return underscoreName;
        }

        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < underscoreName.length(); i++) {
            char ch = underscoreName.charAt(i);
            if ("_".charAt(0) == ch) {
                flag = true;
            } else {
                if (flag) {
                    result.append(Character.toUpperCase(ch));
                    flag = false;
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }
    
    /**
     * <一句话功能简述> 查询添加排序参数
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/9/27
     * @param sortStr pay_amount:DECREASE;shelves_date:DECREASE
     */
    private static void queryAddSort(SearchSourceBuilder sourceBuilder, String sortStr) {
        try{
            if(StringUtils.isBlank(sortStr)){
                return;
            }

            String[] sortArray = sortStr.split(";");
            if(sortArray.length > 0){
                for(String sortSubParam:sortArray){
                    String[] sortSubParamArray = sortSubParam.split(":");
                    String fieldName = camelCaseName(sortSubParamArray[0]);
                    String fieldValue = sortSubParamArray[1];
                    SortOrder sortOrder = null;
                    if("DECREASE".equals(fieldValue)){
                        sortOrder = SortOrder.DESC;
                    }
                    if("INCREASE".equals(fieldValue)){
                        sortOrder = SortOrder.ASC;
                    }
                    logger.debug("fieldName:{},sortOrder:{}", fieldName,sortOrder);
                    if(sortOrder != null){
                        sourceBuilder.sort(new FieldSortBuilder(fieldName).order(sortOrder));
                    }
                }
            }
        }catch (Exception e){
            logger.error("查询添加排序参数异常!", e);
        }
    }

    public static void main(String[] args) {
        String searchStr = "{\n" +
                "\t\t\"dataAmount\": \"10\",\n" +
                "\t\t\"andFilter\": \"min_amount<=50,display_start_date<=1632728060134,(display_end_date='' OR display_end_date>=1632728060134),\",\n" +
                "\t\t\"searchParam\": \"(platform_type:'1' OR platform_type:'3') AND default:'5473' AND is_active:'2' AND item_type:'2' AND (item_state:'6' OR item_state:'7')\",\n" +
                "\t\t\"dataStart\": \"0\",\n" +
                "\t\t\"sort\": \"pay_amount:DECREASE;shelves_date:DECREASE\"\n" +
                "\t}";
        SearchSourceBuilder searchSourceBuilder = SearchUtil.generateEsQuery(JSON.parseObject(searchStr));
        logger.debug("searchSourceBuilder:{}", searchSourceBuilder);
    }

}
