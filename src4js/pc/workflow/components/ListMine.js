import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import * as ListAction from '../actions/list'
import forEach from 'lodash/forEach'
import isEmpty from 'lodash/isEmpty'
import {Synergy} from 'weaPortal';

import {WeaTableRedux,WeaTableRedux_action} from '../../coms/index'

import {
    WeaTop,
    WeaTab,
    WeaLeftTree,
    WeaLeftRightLayout,
    WeaSearchGroup,
    WeaRightMenu,
    WeaPopoverHrm
} from 'ecCom'

import {WeaErrorPage,WeaTools} from 'ecCom'

import {Button,Form} from 'antd'
const createForm = Form.create;
const FormItem = Form.Item;

import cloneDeep from 'lodash/cloneDeep'
import objectAssign from 'object-assign'

import PropTypes from 'react-router/lib/PropTypes'
import Immutable from 'immutable'
const is = Immutable.is;

let _this = null;

class ListMine extends React.Component {
//	static contextTypes = {
//      router: PropTypes.routerShape
//  }
    constructor(props) {
		super(props);
		_this = this;
    }
    componentDidMount() {
    	const {actions} = this.props;
    	actions.setNowRouterWfpath('listMine');
        actions.initDatas({method:"all"});
        actions.doSearch();
    }
    componentWillReceiveProps(nextProps) {
        const keyOld = this.props.location.key;
        const keyNew = nextProps.location.key;
        if(keyOld!==keyNew) {
            const {actions,isClearNowPageStatus} = this.props;
            actions.unmountClear(isClearNowPageStatus);
            actions.isClearNowPageStatus(false);

            actions.setNowRouterWfpath('listMine');
            actions.initDatas({method:"all"});
            actions.doSearch();
        }
        if(window.location.pathname.indexOf('/spa/workflow/index') >= 0 && nextProps.title && document.title !== nextProps.title)
            document.title = nextProps.title;
    }
    shouldComponentUpdate(nextProps,nextState) {
        return !is(this.props.title,nextProps.title)||
        !is(this.props.leftTree,nextProps.leftTree)||
        !is(this.props.leftTreeCount,nextProps.leftTreeCount)||
        !is(this.props.leftTreeCountType,nextProps.leftTreeCountType)||
        !is(this.props.topTab,nextProps.topTab)||
        !is(this.props.topTabCount,nextProps.topTabCount)||
        !is(this.props.columns,nextProps.columns)||
        !is(this.props.loading,nextProps.loading)||
        !is(this.props.operates,nextProps.operates)||
        !is(this.props.searchParams,nextProps.searchParams)||
        !is(this.props.searchParamsAd,nextProps.searchParamsAd)||
        !is(this.props.selectedRowKeys,nextProps.selectedRowKeys)||
        !is(this.props.orderFields,nextProps.orderFields)||
        !is(this.props.showSearchAd,nextProps.showSearchAd)||
        !is(this.props.selectedTreeKeys,nextProps.selectedTreeKeys)||
        !is(this.props.loading,nextProps.loading)||
        !is(this.props.isSpaForm,nextProps.isSpaForm)||
        !is(this.props.conditioninfo,nextProps.conditioninfo)||
        !is(this.props.isClearNowPageStatus,nextProps.isClearNowPageStatus);
    }
    componentWillUnmount(){
    	const {actions,isClearNowPageStatus} = this.props;
        actions.unmountClear(isClearNowPageStatus);
        actions.isClearNowPageStatus(false);
    }
    render() {
        let that = this;
        const isSingle = window.location.pathname.indexOf('/spa/workflow/index') >= 0;
        const {loading,topTab,topTabCount,actions,title,searchParams,showSearchAd,searchParamsAd,showBatchSubmit,phrasesObj,selectedRowKeys} = this.props;
        return (
            <div>
            	{isSingle && <WeaPopoverHrm />}
            	<WeaRightMenu datas={this.getRightMenu()} onClick={this.onRightMenuClick.bind(this)}>
            	<WeaTop
                	title={title}
                	loading={loading}
                	icon={<i className='icon-portal-workflow' />}
                	iconBgcolor='#55D2D4'
                	buttons={this.getButtons()}
                	buttonSpace={10}
                	showDropIcon={true}
                	dropMenuDatas={this.getRightMenu()}
                	onDropMenuClick={this.onRightMenuClick.bind(this)}
                >
                <WeaLeftRightLayout defaultShowLeft={true} leftCom={this.getTree()} leftWidth={25}>
                    <WeaTab
                        buttonsAd={this.getTabButtonsAd()}
                    	searchType={['base','advanced']}
                    	searchsBaseValue={searchParamsAd.get('requestname')}
                        setShowSearchAd={bool=>{actions.setShowSearchAd(bool)}}
                        hideSearchAd={()=> actions.setShowSearchAd(false)}
                        searchsAd={<Form horizontal>{this.getSearchs()}</Form>}
                        showSearchAd={showSearchAd}
                        datas={topTab && topTab.toJS()}
                        selectedKey={searchParams.get('viewcondition')}
                        counts={topTabCount && topTabCount.toJS()}
                        onSearch={v=>{actions.doSearch()}}
                        onSearchChange={v=>{actions.saveOrderFields({requestname:{name:'requestname',value:v}})}}
                        keyParam="viewcondition"  //主键
                        countParam="groupid" //数量
                        onChange={this.changeData.bind(this)} />
                    <WeaTableRedux 
                    	hasOrder={true}
                    	needScroll={true}
                    	/>
                </WeaLeftRightLayout>
                </WeaTop>
                </WeaRightMenu>
                <Synergy pathname='/workflow/listMine' requestid="-1" />
            </div>
        )
    }
    onRightMenuClick(key){
    	const {actions,selectedRowKeys} = this.props;
    	if(key == '0'){
    		actions.doSearch();
    		actions.setShowSearchAd(false)
    	}
    	if(key == '1'){
    		actions.setColSetVisible(true);
    		actions.tableColSet(true)
    	}
    }
    getRightMenu(){
    	const {selectedRowKeys,sharearg,actions} = this.props;
        const hasBatchBtn = sharearg && sharearg.get("hasBatchBtn");
    	let btns = [];
    	btns.push({
    		icon: <i className='icon-Right-menu--search'/>,
    		content:'搜索'
    	});
    	btns.push({
    		icon: <i className='icon-Right-menu-Custom'/>,
    		content:'显示定制列'
    	})
    	return btns
    }
    getSearchs() {
        return [
            (<WeaSearchGroup needTigger={true} title={this.getTitle()} showGroup={this.isShowFields()} items={this.getFields()}/>),
            (<WeaSearchGroup needTigger={true} title={this.getTitle(1)} showGroup={this.isShowFields(1)} items={this.getFields(1)}/>)
        ]
    }
    getTitle(index = 0) {
        const {conditioninfo} = this.props;
        return !isEmpty(conditioninfo.toJS()) && conditioninfo.toJS()[index].title
    }
    isShowFields(index = 0) {
        const {conditioninfo} = this.props;
        return !isEmpty(conditioninfo.toJS()) && conditioninfo.toJS()[index].defaultshow
    }
    // 0 常用条件，1 其他条件
    getFields(index = 0) {
        const {conditioninfo} = this.props;
        const fieldsData = !isEmpty(conditioninfo.toJS()) && conditioninfo.toJS()[index].items;
        let items = [];
        forEach(fieldsData, (field) => {
            items.push({
                com:(<FormItem
                    label={`${field.label}`}
                    labelCol={{span: `${field.labelcol}`}}
                    wrapperCol={{span: `${field.fieldcol}`}}>
                        {WeaTools.switchComponent(this.props, field.key, field.domkey, field)}
                    </FormItem>),
                colSpan:1
            })
        })
        return items;
    }
    changeData(theKey) {
        const {actions} = this.props;
        //const keyArr = theKey?theKey.split("_"):[];
        actions.setShowSearchAd(false);
        actions.doSearch({
            viewcondition:theKey //keyArr.length>1?keyArr[1]:""
        },{});
    }
    getTree() {
        const {leftTree,leftTreeCount,leftTreeCountType,actions,topTab,searchParams,selectedTreeKeys,loading} = this.props;
        return (
            <WeaLeftTree
                datas={leftTree && leftTree.toJS()}
                counts={leftTreeCount && leftTreeCount.toJS()}
                countsType={leftTreeCountType && leftTreeCountType.toJS()}
                selectedKeys={selectedTreeKeys && selectedTreeKeys.toJS()}
                loading={loading}
                onFliterAll={()=>{
                	actions.setShowSearchAd(false);
                	actions.setSelectedTreeKeys([]);
                    actions.saveOrderFields();
                    actions.initDatas({method:"all"});
                    actions.doSearch({
                    	method:'all',
                        viewcondition:0,
                    	workflowid:"",
                        wftype:""
                    });
                }}
                onSelect={(key,topTabCount,countsType)=>{
                	actions.setShowSearchAd(false);
                	let viewc = '0';
                	actions.setSelectedTreeKeys([key]);
                	topTab.map(t=>{
                        if(countsType && countsType.name && t.get('groupid') == countsType.name) viewc = t.get('viewcondition')
                	})
                	const workflowid = key.indexOf("wf_")===0 ? key.substring(3) : '';
                	const workflowtype = key.indexOf("type_")===0 ? key.substring(5) : '';
                	const fieldsObj = {
                		workflowid:{name:'workflowid',value:workflowid},
                		workflowtype:{name:'workflowtype',value:workflowtype}
                	};
                	actions.saveOrderFields(fieldsObj);
                    actions.doSearch({
                        method:key.indexOf("type_")===0 ? "reqeustbytype" : "reqeustbywfid",
                        wftype: workflowtype,
                        workflowid: workflowid,
                        viewcondition: viewc,
                    });
                }} />
        )
    }
    getTabButtonsAd() {
    	const {actions} = this.props;
        return [
            (<Button type="primary" onClick={()=>{actions.doSearch();actions.setShowSearchAd(false)}}>搜索</Button>),
            (<Button type="ghost" onClick={()=>{actions.saveOrderFields({})}}>重置</Button>),
            (<Button type="ghost" onClick={()=>{actions.setShowSearchAd(false)}}>取消</Button>)
        ]
    }
    getButtons() {
    	const {selectedRowKeys,actions} = this.props;
    	let btns = [];
        return btns
    }
}

class MyErrorHandler extends React.Component {
    render(){
        const hasErrorMsg = this.props.error && this.props.error!=="";
        return (
            <WeaErrorPage msg={hasErrorMsg?this.props.error:"对不起，该页面异常，请联系管理员！"} />
        );
    }
}

ListMine = WeaTools.tryCatch(React, MyErrorHandler, {error: ""})(ListMine);

ListMine = createForm({
	onFieldsChange(props, fields) {
    	let _fields = {...fields};
    	for(let k in fields){
    		if(fields[k].value.indexOf('_@_') >= 0){
    			let newValue =  fields[k].value.split('_@_');
	    		_fields[k].value = newValue[0];
    		}
    	}
        props.actions.saveOrderFields({...props.orderFields.toJS(), ...fields,..._fields});
    },
	mapPropsToFields(props) {
		return props.orderFields.toJS();
  	}
})(ListMine);

function mapStateToProps(state) {
    const {workflowlistMine,WeaTableRedux_state} = state;
	const name = workflowlistMine.get('dataKey') ? workflowlistMine.get('dataKey').split('_')[0] : 'init';
    return {
        title: workflowlistMine.get('title'),
		leftTree: workflowlistMine.get('leftTree'),
		leftTreeCount: workflowlistMine.get('leftTreeCount'),
		leftTreeCountType: workflowlistMine.get('leftTreeCountType'),
		topTab: workflowlistMine.get('topTab'),
		topTabCount: workflowlistMine.get('topTabCount'),
		searchParams: workflowlistMine.get('searchParams'),
		searchParamsAd: workflowlistMine.get('searchParamsAd'),
		orderFields: workflowlistMine.get('orderFields'),
		showSearchAd: workflowlistMine.get('showSearchAd'),
		selectedTreeKeys: workflowlistMine.get('selectedTreeKeys'),
		isSpaForm: workflowlistMine.get('isSpaForm'),
		isClearNowPageStatus: workflowlistMine.get('isClearNowPageStatus'),
		sortParams: workflowlistMine.get('sortParams'),
		conditioninfo: workflowlistMine.get('conditioninfo'),
		showBatchSubmit: workflowlistMine.get('showBatchSubmit'),
		phrasesObj: workflowlistMine.get('phrasesObj'),
		sharearg: workflowlistMine.get('sharearg'),
		//table
		loading: WeaTableRedux_state.getIn([name,'loading']),
		selectedRowKeys: WeaTableRedux_state.getIn([name,'selectedRowKeys']),
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators({...ListAction,...WeaTableRedux_action}, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ListMine);
