//编辑行号
var editIndex = undefined;
//列数据
var columns = [[
    {
        field: 'goodsuuid', title: '商品编号', width: 100, editor: {
        type: 'numberbox', options: {
            disabled: true
        }
    }
    },
    {
        field: 'goodsname', title: '商品名称', width: 180, editor: {
        type: 'combobox',
        options: {
            valueField: 'name',
            textField: 'name',
            url: 'goods_simpleList',
            required: true,
            onSelect: function (record) {
                //价格
                if (type == 1) {
                    var price = record.inprice;
                }
                if (type == 2) {
                    var price = record.outprice;
                }
                var uuid = record.uuid;

                //获取编辑器，商品编号
                var goodsuuidEditor = getEditor('goodsuuid');
                //设置商品编号
                $(goodsuuidEditor.target).numberbox('setValue', uuid);
                //获取价格编辑器
                var priceEditor = getEditor('price');
                //设置商品编号
                $(priceEditor.target).numberbox('setValue', price);

                var numEditor = getEditor('num');
                //定位光标到输入框中
                $(numEditor.target).select();

                //绑定keyup事件
                bindGridEvent();

                cal();
                sum();

            }
        }
    }
    },
    {
        field: 'price',
        title: '价格',
        width: 100,
        align: 'right',
        editor: {type: 'numberbox', options: {precision: 2}}
    },
    {field: 'num', title: '数量', width: 100, align: 'right', editor: 'numberbox'},
    {
        field: 'money',
        title: '金额',
        width: 100,
        editor: {type: 'numberbox', options: {precision: 2, disabled: true}}
    }
]];

//结束编辑
function endEditing() {
    if (editIndex == undefined) {
        return true
    }
    if ($('#ordersGrid').datagrid('validateRow', editIndex)) {
        var ed = $('#ordersGrid').datagrid('getEditor', {index: editIndex, field: 'goodsname'});
        var goodsname = $(ed.target).combobox('getText');
        $('#ordersGrid').datagrid('getRows')[editIndex]['goodsname'] = goodsname;
        $('#ordersGrid').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        $.messager.alert("警告", "请检查数据", 'info');
        return false;
    }
}

//点击行事件
function onClickRow(index) {
    if (editIndex != index) {
        if (endEditing()) {
            $('#ordersGrid').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#ordersGrid').datagrid('selectRow', editIndex);
        }
        bindGridEvent();
    }
}

//增加行
function append() {
    if (endEditing()) {
        $('#ordersGrid').datagrid('appendRow', {'num': 0, 'money': 0});
        editIndex = $('#ordersGrid').datagrid('getRows').length - 1;
        $('#ordersGrid').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);
        bindGridEvent();
    }
}

//删除行
function removeit() {
    if (editIndex == undefined) {
        return
    }
    $('#ordersGrid').datagrid('cancelEdit', editIndex)
        .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    sum();
}

//接受变更
function accept() {
    cal();
    if (endEditing()) {
        $('#ordersGrid').datagrid('acceptChanges');
    }
    sum();
}

//回滚变更
function reject() {
    $('#ordersGrid').datagrid('rejectChanges');
    editIndex = undefined;
}

//提交
var supplie = "";

function submitOrders() {
    if (endEditing()) {

        //判断是否选择供应商
        if (!(supplie * 1 > 0)) {
            $.messager.alert("警告", "请选择供应商", 'info');
            return;
        }
        //获取行数据
        var rows = $('#ordersGrid').datagrid('getRows');
        //判断是否有数据
        if (rows.length < 1) {
            $.messager.alert("警告", "商品为空", 'info');
            return;
        }
        //拼接数据
        var submitData = $('#orderForm').serializeJSON();
        submitData.json = JSON.stringify(rows);

        //发起请求
        $.post('orders_add?t1.type=' + type, submitData, function (data) {
            $.messager.alert('提示', data.message, 'info', function () {
                if (data.success) {
                    //清空供应商
                    $('#orderForm').form('clear');
                    //清空表格
                    $('#ordersGrid').datagrid('loadData', {total: 0, rows: [], footer: [{num: '合计', money: '0'}]});
                    //关闭窗口
                    $("#editOrders").window("close");
                    //刷新列表
                    $("#grid").datagrid("reload");
                }
            });
        }, 'json')
    }
}

//获取编辑对象
function getEditor(_field) {
    return ($('#ordersGrid').datagrid('getEditor', {index: editIndex, field: _field}));
}

//绑定时间
function bindGridEvent() {
    var numEditor = getEditor('num');
    $(numEditor.target).bind('focus', function () {
        cal();
        sum();
    });
}

/**
 * 计算金额
 */
function cal() {
    //1. 获取价格
    //  1. 1 获取价格的编辑器
    var priceEditor = getEditor('price');
    //  1. 2 取出价格
    var price = $(priceEditor.target).numberbox('getValue');

    //2. 获取数量
    var numEditor = getEditor('num');
    var num = $(numEditor.target).numberbox('getValue');

    //3. 计算金额
    //toFixed js自带的方法，只保留小数点后2位有效数字
    var total = (num * price).toFixed(2);

    //4. 把金额显示到列中
    var moneyEditor = getEditor('money');

    //total这时没有进入grid里的数据
    $(moneyEditor.target).numberbox('setValue', total);

    //让数据进入到grid里面去
    $('#ordersGrid').datagrid('getRows')[editIndex].money = total;
}

/**
 * 计算合计金额
 */
function sum() {
    var rows = $('#ordersGrid').datagrid('getRows');
    //循环遍历
    var totalMoney = 0;
    $.each(rows, function (i, row) {
        totalMoney += parseFloat(row.money);
        //return false;退出循环
    });
    totalMoney = totalMoney.toFixed(2);
    //显示到合计中去
    $('#ordersGrid').datagrid('reloadFooter', [{num: '合计', money: totalMoney + ''}]);
}

//打开添加窗口
function openAdd() {
    //供应商下拉表格
    if (type == 1) $("#comboName").html('供应商');
    if (type == 2) $("#comboName").html('客户');
    $('#supplier').combogrid({
        panelWidth: 700,
        idField: 'uuid', //要提交的供应商编号
        textField: 'name',//显示的供应商名称
        url: 'supplier_list?t1.type=' + type,
        fitColumns: true,
        required: true,
        mode: 'remote',
        columns: [[
            {field: 'name', title: '名称', width: 100},
            {field: 'address', title: '联系地址', width: 100},
            {field: 'contact', title: '联系人', width: 100},
            {field: 'tele', title: '联系电话', width: 100},
            {field: 'email', title: '邮件地址', width: 100}
        ]],
        onSelect: function (data) {
            supplie = data;
        }
    });

    //$('#supplier').combogrid('reload');
    //加载行脚
    $('#ordersGrid').datagrid('reloadFooter', [{num: '合计', money: '0'}]);
    $("#editOrders").window("open")
}