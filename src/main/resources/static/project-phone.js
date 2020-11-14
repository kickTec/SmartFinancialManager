$('#tab tr').each(function(i){
    // 折叠过长文字
    $(this).children('th').each(function(j){  // 遍历 tr 的各个 td
        if(j==1){
            $(this).html(cutString($(this).text(),50));
            $(this).css("width",100);
        }
    });
});