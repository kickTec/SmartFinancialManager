package com.kenick.generate.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConstantDataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConstantDataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andConstantIdIsNull() {
            addCriterion("constant_id is null");
            return (Criteria) this;
        }

        public Criteria andConstantIdIsNotNull() {
            addCriterion("constant_id is not null");
            return (Criteria) this;
        }

        public Criteria andConstantIdEqualTo(String value) {
            addCriterion("constant_id =", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdNotEqualTo(String value) {
            addCriterion("constant_id <>", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdGreaterThan(String value) {
            addCriterion("constant_id >", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdGreaterThanOrEqualTo(String value) {
            addCriterion("constant_id >=", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdLessThan(String value) {
            addCriterion("constant_id <", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdLessThanOrEqualTo(String value) {
            addCriterion("constant_id <=", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdLike(String value) {
            addCriterion("constant_id like", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdNotLike(String value) {
            addCriterion("constant_id not like", value, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdIn(List<String> values) {
            addCriterion("constant_id in", values, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdNotIn(List<String> values) {
            addCriterion("constant_id not in", values, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdBetween(String value1, String value2) {
            addCriterion("constant_id between", value1, value2, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantIdNotBetween(String value1, String value2) {
            addCriterion("constant_id not between", value1, value2, "constantId");
            return (Criteria) this;
        }

        public Criteria andConstantDescIsNull() {
            addCriterion("constant_desc is null");
            return (Criteria) this;
        }

        public Criteria andConstantDescIsNotNull() {
            addCriterion("constant_desc is not null");
            return (Criteria) this;
        }

        public Criteria andConstantDescEqualTo(String value) {
            addCriterion("constant_desc =", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescNotEqualTo(String value) {
            addCriterion("constant_desc <>", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescGreaterThan(String value) {
            addCriterion("constant_desc >", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescGreaterThanOrEqualTo(String value) {
            addCriterion("constant_desc >=", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescLessThan(String value) {
            addCriterion("constant_desc <", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescLessThanOrEqualTo(String value) {
            addCriterion("constant_desc <=", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescLike(String value) {
            addCriterion("constant_desc like", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescNotLike(String value) {
            addCriterion("constant_desc not like", value, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescIn(List<String> values) {
            addCriterion("constant_desc in", values, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescNotIn(List<String> values) {
            addCriterion("constant_desc not in", values, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescBetween(String value1, String value2) {
            addCriterion("constant_desc between", value1, value2, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantDescNotBetween(String value1, String value2) {
            addCriterion("constant_desc not between", value1, value2, "constantDesc");
            return (Criteria) this;
        }

        public Criteria andConstantNameIsNull() {
            addCriterion("constant_name is null");
            return (Criteria) this;
        }

        public Criteria andConstantNameIsNotNull() {
            addCriterion("constant_name is not null");
            return (Criteria) this;
        }

        public Criteria andConstantNameEqualTo(String value) {
            addCriterion("constant_name =", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameNotEqualTo(String value) {
            addCriterion("constant_name <>", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameGreaterThan(String value) {
            addCriterion("constant_name >", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameGreaterThanOrEqualTo(String value) {
            addCriterion("constant_name >=", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameLessThan(String value) {
            addCriterion("constant_name <", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameLessThanOrEqualTo(String value) {
            addCriterion("constant_name <=", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameLike(String value) {
            addCriterion("constant_name like", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameNotLike(String value) {
            addCriterion("constant_name not like", value, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameIn(List<String> values) {
            addCriterion("constant_name in", values, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameNotIn(List<String> values) {
            addCriterion("constant_name not in", values, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameBetween(String value1, String value2) {
            addCriterion("constant_name between", value1, value2, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantNameNotBetween(String value1, String value2) {
            addCriterion("constant_name not between", value1, value2, "constantName");
            return (Criteria) this;
        }

        public Criteria andConstantValueIsNull() {
            addCriterion("constant_value is null");
            return (Criteria) this;
        }

        public Criteria andConstantValueIsNotNull() {
            addCriterion("constant_value is not null");
            return (Criteria) this;
        }

        public Criteria andConstantValueEqualTo(String value) {
            addCriterion("constant_value =", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueNotEqualTo(String value) {
            addCriterion("constant_value <>", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueGreaterThan(String value) {
            addCriterion("constant_value >", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueGreaterThanOrEqualTo(String value) {
            addCriterion("constant_value >=", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueLessThan(String value) {
            addCriterion("constant_value <", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueLessThanOrEqualTo(String value) {
            addCriterion("constant_value <=", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueLike(String value) {
            addCriterion("constant_value like", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueNotLike(String value) {
            addCriterion("constant_value not like", value, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueIn(List<String> values) {
            addCriterion("constant_value in", values, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueNotIn(List<String> values) {
            addCriterion("constant_value not in", values, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueBetween(String value1, String value2) {
            addCriterion("constant_value between", value1, value2, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantValueNotBetween(String value1, String value2) {
            addCriterion("constant_value not between", value1, value2, "constantValue");
            return (Criteria) this;
        }

        public Criteria andConstantStateIsNull() {
            addCriterion("constant_state is null");
            return (Criteria) this;
        }

        public Criteria andConstantStateIsNotNull() {
            addCriterion("constant_state is not null");
            return (Criteria) this;
        }

        public Criteria andConstantStateEqualTo(Integer value) {
            addCriterion("constant_state =", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateNotEqualTo(Integer value) {
            addCriterion("constant_state <>", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateGreaterThan(Integer value) {
            addCriterion("constant_state >", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("constant_state >=", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateLessThan(Integer value) {
            addCriterion("constant_state <", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateLessThanOrEqualTo(Integer value) {
            addCriterion("constant_state <=", value, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateIn(List<Integer> values) {
            addCriterion("constant_state in", values, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateNotIn(List<Integer> values) {
            addCriterion("constant_state not in", values, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateBetween(Integer value1, Integer value2) {
            addCriterion("constant_state between", value1, value2, "constantState");
            return (Criteria) this;
        }

        public Criteria andConstantStateNotBetween(Integer value1, Integer value2) {
            addCriterion("constant_state not between", value1, value2, "constantState");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateIsNull() {
            addCriterion("modify_date is null");
            return (Criteria) this;
        }

        public Criteria andModifyDateIsNotNull() {
            addCriterion("modify_date is not null");
            return (Criteria) this;
        }

        public Criteria andModifyDateEqualTo(Date value) {
            addCriterion("modify_date =", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotEqualTo(Date value) {
            addCriterion("modify_date <>", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateGreaterThan(Date value) {
            addCriterion("modify_date >", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_date >=", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateLessThan(Date value) {
            addCriterion("modify_date <", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateLessThanOrEqualTo(Date value) {
            addCriterion("modify_date <=", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateIn(List<Date> values) {
            addCriterion("modify_date in", values, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotIn(List<Date> values) {
            addCriterion("modify_date not in", values, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateBetween(Date value1, Date value2) {
            addCriterion("modify_date between", value1, value2, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotBetween(Date value1, Date value2) {
            addCriterion("modify_date not between", value1, value2, "modifyDate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}