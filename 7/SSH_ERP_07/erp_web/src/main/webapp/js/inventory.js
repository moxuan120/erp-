var oper = Request['oper'];
var url='inventory_list';
if(oper=='doCheck') url+='?t1.state=0';
$(function () {
    // 初始化表格数据
    $("#grid").datagrid({
        url: url,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        toolbar: '#tb',
        fitColumns: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'goodsName', title: '商品', width: 100},
            {field: 'storeName', title: '仓库', width: 100},
            {field: 'num', title: '数量', width: 100},
            {field: 'type', title: '类型', width: 100,formatter:function(value){
  		    	if(value == 0) return "完美";
  		    	if(value == 1) return "盈";
  		    	if(value == 2) return "亏";
  		    }},
            {field: 'createtime', title: '登记时间', width: 120},
            {field: 'checktime', title: '审核时间', width: 120},
            {field: 'createrName', title: '登记人', width: 100},
            {field: 'checkerName', title: '审核人', width: 100},
            {field: 'state', title: '状态', width: 100,formatter:formatType},
            {field: 'remark', title: '备注', width: 100}
        ]]
    });
    
    if (oper=='doCheck') {
    	$("#grid").datagrid({
        onDblClickRow: function (rowIndex, rowData) {
        	//给详情赋值
            $('#uuid').val(rowData.uuid);
            $('#storeuuid').html(rowData.storeName);
            $('#goodsuuid').html(rowData.goodsName);
            $('#num').html(rowData.num);
            $('#type').html(formatType);

        	$("#checkWindow").dialog('open')
        }
    	});
	}
    // 初始化添加窗口
    $("#addWindow").dialog({
        width: 320,
        title: '盘盈盘亏登记',
        closed: true,
        cache: false,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: submitAddForm
        }, {
            text: '关闭',
            iconCls: 'icon-cancel',
            handler: function() {
            	$("#addWindow").window('close');
			}
        }]
    });
    // 初始化审核窗口
    $("#checkWindow").dialog({
    	width: 320,
    	title: '盘盈盘亏审核',
    	closed: true,
    	cache: false,
    	modal: true,
    	buttons: [{
    		text: '保存',
    		iconCls: 'icon-save',
    		handler: submitCheckForm
    	}, {
    		text: '关闭',
    		iconCls: 'icon-cancel',
    		handler: function() {
            	$("#checkWindow").window('close');
			}
    	}]
    });
    if ('doInput' == oper) {
	    // pagination自定义工具栏按钮
	    var paginationBtn = new Array();
	    paginationBtn.push({
	        iconCls: 'icon-add', text: '盘盈盘亏登记', handler: function () {
	            method = "add";
	            $("#addForm").form('clear');
	            $("#addWindow").window('open');
	        }
	    });
	    $('#grid').datagrid('getPager').pagination({
	        buttons:paginationBtn
	    });
    }
});

function refresh() {
    var data = $("#searchForm").serializeJSON();
    $("#grid").datagrid('load', data)
}

function submitAddForm() {
    $('#addForm').form('submit', {
        url: 'inventory_add',
        onSubmit: function () {
            return $(this).form('enableValidation').form('validate');
        },
        success: function (data) {
            var data = eval('(' + data + ')'); // json转化成js对象
            $.messager.alert('提示', data.message, 'info', function () {
                if (data.success) {
                    $("#grid").datagrid('reload');
                    $("#addWindow").window('close');
                }
            });
        }
    });
}
function submitCheckForm() {
	$('#checkForm').form('submit', {
		url: 'inventory_doCheck',
		onSubmit: function () {
			return $(this).form('enableValidation').form('validate');
		},
		success: function (data) {
			var data = eval('(' + data + ')'); // json转化成js对象
			$.messager.alert('提示', data.message, 'info', function () {
				if (data.success) {
					$("#grid").datagrid('reload');
					$("#checkWindow").window('close');
				}
			});
		}
	});
}

function clearSearchForm() {
    $("#searchForm").form('clear');
}

function formatType(value) {
        if(value == 0) return "已登记";
        if(value == 1) return "已审核";
        if(value == 2) return "已完成";
}