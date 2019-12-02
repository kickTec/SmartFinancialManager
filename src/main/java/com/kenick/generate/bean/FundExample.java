package com.kenick.generate.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FundExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FundExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andCurTimeIsNull() {
            addCriterion("cur_time is null");
            return (Criteria) this;
        }

        public Criteria andCurTimeIsNotNull() {
            addCriterion("cur_time is not null");
            return (Criteria) this;
        }

        public Criteria andCurTimeEqualTo(String value) {
            addCriterion("cur_time =", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeNotEqualTo(String value) {
            addCriterion("cur_time <>", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeGreaterThan(String value) {
            addCriterion("cur_time >", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeGreaterThanOrEqualTo(String value) {
            addCriterion("cur_time >=", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeLessThan(String value) {
            addCriterion("cur_time <", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeLessThanOrEqualTo(String value) {
            addCriterion("cur_time <=", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeLike(String value) {
            addCriterion("cur_time like", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeNotLike(String value) {
            addCriterion("cur_time not like", value, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeIn(List<String> values) {
            addCriterion("cur_time in", values, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeNotIn(List<String> values) {
            addCriterion("cur_time not in", values, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeBetween(String value1, String value2) {
            addCriterion("cur_time between", value1, value2, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurTimeNotBetween(String value1, String value2) {
            addCriterion("cur_time not between", value1, value2, "curTime");
            return (Criteria) this;
        }

        public Criteria andCurNetValueIsNull() {
            addCriterion("cur_net_value is null");
            return (Criteria) this;
        }

        public Criteria andCurNetValueIsNotNull() {
            addCriterion("cur_net_value is not null");
            return (Criteria) this;
        }

        public Criteria andCurNetValueEqualTo(Double value) {
            addCriterion("cur_net_value =", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueNotEqualTo(Double value) {
            addCriterion("cur_net_value <>", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueGreaterThan(Double value) {
            addCriterion("cur_net_value >", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueGreaterThanOrEqualTo(Double value) {
            addCriterion("cur_net_value >=", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueLessThan(Double value) {
            addCriterion("cur_net_value <", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueLessThanOrEqualTo(Double value) {
            addCriterion("cur_net_value <=", value, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueIn(List<Double> values) {
            addCriterion("cur_net_value in", values, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueNotIn(List<Double> values) {
            addCriterion("cur_net_value not in", values, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueBetween(Double value1, Double value2) {
            addCriterion("cur_net_value between", value1, value2, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurNetValueNotBetween(Double value1, Double value2) {
            addCriterion("cur_net_value not between", value1, value2, "curNetValue");
            return (Criteria) this;
        }

        public Criteria andCurGainIsNull() {
            addCriterion("cur_gain is null");
            return (Criteria) this;
        }

        public Criteria andCurGainIsNotNull() {
            addCriterion("cur_gain is not null");
            return (Criteria) this;
        }

        public Criteria andCurGainEqualTo(Double value) {
            addCriterion("cur_gain =", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainNotEqualTo(Double value) {
            addCriterion("cur_gain <>", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainGreaterThan(Double value) {
            addCriterion("cur_gain >", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainGreaterThanOrEqualTo(Double value) {
            addCriterion("cur_gain >=", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainLessThan(Double value) {
            addCriterion("cur_gain <", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainLessThanOrEqualTo(Double value) {
            addCriterion("cur_gain <=", value, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainIn(List<Double> values) {
            addCriterion("cur_gain in", values, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainNotIn(List<Double> values) {
            addCriterion("cur_gain not in", values, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainBetween(Double value1, Double value2) {
            addCriterion("cur_gain between", value1, value2, "curGain");
            return (Criteria) this;
        }

        public Criteria andCurGainNotBetween(Double value1, Double value2) {
            addCriterion("cur_gain not between", value1, value2, "curGain");
            return (Criteria) this;
        }

        public Criteria andLastNetValueIsNull() {
            addCriterion("last_net_value is null");
            return (Criteria) this;
        }

        public Criteria andLastNetValueIsNotNull() {
            addCriterion("last_net_value is not null");
            return (Criteria) this;
        }

        public Criteria andLastNetValueEqualTo(Double value) {
            addCriterion("last_net_value =", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueNotEqualTo(Double value) {
            addCriterion("last_net_value <>", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueGreaterThan(Double value) {
            addCriterion("last_net_value >", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueGreaterThanOrEqualTo(Double value) {
            addCriterion("last_net_value >=", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueLessThan(Double value) {
            addCriterion("last_net_value <", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueLessThanOrEqualTo(Double value) {
            addCriterion("last_net_value <=", value, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueIn(List<Double> values) {
            addCriterion("last_net_value in", values, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueNotIn(List<Double> values) {
            addCriterion("last_net_value not in", values, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueBetween(Double value1, Double value2) {
            addCriterion("last_net_value between", value1, value2, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastNetValueNotBetween(Double value1, Double value2) {
            addCriterion("last_net_value not between", value1, value2, "lastNetValue");
            return (Criteria) this;
        }

        public Criteria andLastGainIsNull() {
            addCriterion("last_gain is null");
            return (Criteria) this;
        }

        public Criteria andLastGainIsNotNull() {
            addCriterion("last_gain is not null");
            return (Criteria) this;
        }

        public Criteria andLastGainEqualTo(Double value) {
            addCriterion("last_gain =", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainNotEqualTo(Double value) {
            addCriterion("last_gain <>", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainGreaterThan(Double value) {
            addCriterion("last_gain >", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainGreaterThanOrEqualTo(Double value) {
            addCriterion("last_gain >=", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainLessThan(Double value) {
            addCriterion("last_gain <", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainLessThanOrEqualTo(Double value) {
            addCriterion("last_gain <=", value, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainIn(List<Double> values) {
            addCriterion("last_gain in", values, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainNotIn(List<Double> values) {
            addCriterion("last_gain not in", values, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainBetween(Double value1, Double value2) {
            addCriterion("last_gain between", value1, value2, "lastGain");
            return (Criteria) this;
        }

        public Criteria andLastGainNotBetween(Double value1, Double value2) {
            addCriterion("last_gain not between", value1, value2, "lastGain");
            return (Criteria) this;
        }

        public Criteria andGainTotalIsNull() {
            addCriterion("gain_total is null");
            return (Criteria) this;
        }

        public Criteria andGainTotalIsNotNull() {
            addCriterion("gain_total is not null");
            return (Criteria) this;
        }

        public Criteria andGainTotalEqualTo(BigDecimal value) {
            addCriterion("gain_total =", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalNotEqualTo(BigDecimal value) {
            addCriterion("gain_total <>", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalGreaterThan(BigDecimal value) {
            addCriterion("gain_total >", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("gain_total >=", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalLessThan(BigDecimal value) {
            addCriterion("gain_total <", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("gain_total <=", value, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalIn(List<BigDecimal> values) {
            addCriterion("gain_total in", values, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalNotIn(List<BigDecimal> values) {
            addCriterion("gain_total not in", values, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gain_total between", value1, value2, "gainTotal");
            return (Criteria) this;
        }

        public Criteria andGainTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gain_total not between", value1, value2, "gainTotal");
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