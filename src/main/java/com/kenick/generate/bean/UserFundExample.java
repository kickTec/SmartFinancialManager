package com.kenick.generate.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFundExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserFundExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andFundCodeIsNull() {
            addCriterion("fund_code is null");
            return (Criteria) this;
        }

        public Criteria andFundCodeIsNotNull() {
            addCriterion("fund_code is not null");
            return (Criteria) this;
        }

        public Criteria andFundCodeEqualTo(String value) {
            addCriterion("fund_code =", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeNotEqualTo(String value) {
            addCriterion("fund_code <>", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeGreaterThan(String value) {
            addCriterion("fund_code >", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeGreaterThanOrEqualTo(String value) {
            addCriterion("fund_code >=", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeLessThan(String value) {
            addCriterion("fund_code <", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeLessThanOrEqualTo(String value) {
            addCriterion("fund_code <=", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeLike(String value) {
            addCriterion("fund_code like", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeNotLike(String value) {
            addCriterion("fund_code not like", value, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeIn(List<String> values) {
            addCriterion("fund_code in", values, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeNotIn(List<String> values) {
            addCriterion("fund_code not in", values, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeBetween(String value1, String value2) {
            addCriterion("fund_code between", value1, value2, "fundCode");
            return (Criteria) this;
        }

        public Criteria andFundCodeNotBetween(String value1, String value2) {
            addCriterion("fund_code not between", value1, value2, "fundCode");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andWxCodeIsNull() {
            addCriterion("wx_code is null");
            return (Criteria) this;
        }

        public Criteria andWxCodeIsNotNull() {
            addCriterion("wx_code is not null");
            return (Criteria) this;
        }

        public Criteria andWxCodeEqualTo(String value) {
            addCriterion("wx_code =", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeNotEqualTo(String value) {
            addCriterion("wx_code <>", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeGreaterThan(String value) {
            addCriterion("wx_code >", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeGreaterThanOrEqualTo(String value) {
            addCriterion("wx_code >=", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeLessThan(String value) {
            addCriterion("wx_code <", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeLessThanOrEqualTo(String value) {
            addCriterion("wx_code <=", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeLike(String value) {
            addCriterion("wx_code like", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeNotLike(String value) {
            addCriterion("wx_code not like", value, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeIn(List<String> values) {
            addCriterion("wx_code in", values, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeNotIn(List<String> values) {
            addCriterion("wx_code not in", values, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeBetween(String value1, String value2) {
            addCriterion("wx_code between", value1, value2, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxCodeNotBetween(String value1, String value2) {
            addCriterion("wx_code not between", value1, value2, "wxCode");
            return (Criteria) this;
        }

        public Criteria andWxNameIsNull() {
            addCriterion("wx_name is null");
            return (Criteria) this;
        }

        public Criteria andWxNameIsNotNull() {
            addCriterion("wx_name is not null");
            return (Criteria) this;
        }

        public Criteria andWxNameEqualTo(String value) {
            addCriterion("wx_name =", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotEqualTo(String value) {
            addCriterion("wx_name <>", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameGreaterThan(String value) {
            addCriterion("wx_name >", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameGreaterThanOrEqualTo(String value) {
            addCriterion("wx_name >=", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLessThan(String value) {
            addCriterion("wx_name <", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLessThanOrEqualTo(String value) {
            addCriterion("wx_name <=", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameLike(String value) {
            addCriterion("wx_name like", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotLike(String value) {
            addCriterion("wx_name not like", value, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameIn(List<String> values) {
            addCriterion("wx_name in", values, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotIn(List<String> values) {
            addCriterion("wx_name not in", values, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameBetween(String value1, String value2) {
            addCriterion("wx_name between", value1, value2, "wxName");
            return (Criteria) this;
        }

        public Criteria andWxNameNotBetween(String value1, String value2) {
            addCriterion("wx_name not between", value1, value2, "wxName");
            return (Criteria) this;
        }

        public Criteria andFundNameIsNull() {
            addCriterion("fund_name is null");
            return (Criteria) this;
        }

        public Criteria andFundNameIsNotNull() {
            addCriterion("fund_name is not null");
            return (Criteria) this;
        }

        public Criteria andFundNameEqualTo(String value) {
            addCriterion("fund_name =", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameNotEqualTo(String value) {
            addCriterion("fund_name <>", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameGreaterThan(String value) {
            addCriterion("fund_name >", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameGreaterThanOrEqualTo(String value) {
            addCriterion("fund_name >=", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameLessThan(String value) {
            addCriterion("fund_name <", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameLessThanOrEqualTo(String value) {
            addCriterion("fund_name <=", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameLike(String value) {
            addCriterion("fund_name like", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameNotLike(String value) {
            addCriterion("fund_name not like", value, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameIn(List<String> values) {
            addCriterion("fund_name in", values, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameNotIn(List<String> values) {
            addCriterion("fund_name not in", values, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameBetween(String value1, String value2) {
            addCriterion("fund_name between", value1, value2, "fundName");
            return (Criteria) this;
        }

        public Criteria andFundNameNotBetween(String value1, String value2) {
            addCriterion("fund_name not between", value1, value2, "fundName");
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