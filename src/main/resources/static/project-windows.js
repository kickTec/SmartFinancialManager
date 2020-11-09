$('#tab tr').each(function(i){
    $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
        if(j==2){
            console.log("第"+(i+1)+"行，第"+(j+1)+"个td的值："+$(this).text()+"。");
            $(this).html(cutString($(this).text(),100));
        }
    });
});