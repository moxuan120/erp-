var type = Request['type'];
var from = Request['from'];
$(function () {

    //加载表格
    $('#grid').datagrid({
        url: chartUrl,
        toolbar: '#tb',
        fit:true,
        fitColumns: true,
        singleSelect: true,
        columns: columns,
        onLoadSuccess: function (data) {
            //刷新饼图
            showPie(data.rows);
            showCont(data.rows);
            showLine(data.rows);
        }
    });
});

//加载饼图
function showPie(data) {
    $("#pie").highcharts({
        chart: {
            //plotBackgroundColor: null,
            //plotBorderWidth: null,
            //plotShadow: false,
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: chartTitle
        },
        tooltip: {
            headerFormat: '{series.name}<br>',
            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: chartTitle,
            data: data
        }],
        credits: {
            enabled: false
        }
    })
}

//加载折线图
function showLine(data) {
    $("#line").highcharts({
        title: {
            text: chartTitle,
            x: -20
        },
        xAxis: {
            categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
        },
        yAxis: {
            title: {
                text: '金额（元）'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '元'
        },
        legend: {
            layout: 'vertical',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
        },
        series: [{
            name: '销售额',
            data: data

        }],
        credits: {
            enabled: false
        }
    });
}

//加载柱状图
function showCont(data) {
	$('#container').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: chartTitle
        },
        xAxis: {
        	categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
        },
        yAxis: {
            min: 0,
            title: {
                text: '金额（元）'
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            pointFormat: '<b>{point.y:.1f} 元</b>'
        },
        series: [{
            name: 'Population',
            data: data,
            dataLabels: {
                enabled: true,
                rotation: -90,
                color: '#FFFFFF',
                align: 'right',
                format: '{point.y:.1f}', // one decimal
                y: 10, // 10 pixels down from the top
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        }]
    });
}


