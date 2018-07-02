import React, {Component,PropTypes} from 'react';
import {connect} from "react-redux";
import {addTabAction} from "MODULES/ducks/Layout";
import {TestPlanContentView} from "../../Test";
import {deleteTestPlan,getTestPlanList, newTestPlan} from "../../../services/TestPlanService";
import {setTestPlanFilter} from "../../../modules/ducks/TestPlan";
import TestPlanListComponent from "../components/TestPlanListComponent";
//import {deleteContract, newContract} from "../../../services/ContractService";

const mapStateToProps = (state) => {
    return {
        dataSource: Object.values(state.TestPlan.listMap).filter(state.TestPlan.listFilter),
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        showContent: (id) => {
            const key = "测试方案" + id;
            dispatch(addTabAction(key, '测试方案详情', TestPlanContentView,{id:id}));
//            dispatch(setConsignContent())
        },
        setListFilter: (listFilter) => dispatch(setTestPlanFilter(listFilter)),
        getTestPlanList: () => getTestPlanList(dispatch),
        deleteTestPlan: (id) => deleteTestPlan(dispatch,id),
        newTestPlan: () => newTestPlan(dispatch)
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(TestPlanListComponent);