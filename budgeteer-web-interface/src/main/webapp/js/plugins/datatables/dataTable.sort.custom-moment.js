/**Custom Sort for dateRanges */
(function($) {
    /* Add type detection */
    $.fn.dataTable.ext.type.detect.unshift(function (d) {
        /* Null and empty values are acceptable */
        if (d === '' || d === null) {
            return 'moment-daterange';
        }
        var reg = /\d\d\.\d\d.\d\d/;
        var matcher = reg.exec(d);
        return matcher !== null && moment(matcher[0], 'D.MM.YY', null, true).isValid()? 'moment-daterange' : null;
    });

    /* Add sorting method - use an integer for the sorting */
    $.fn.dataTable.ext.type.order['moment-daterange-pre' ] = function (d) {
        var reg = /\d\d\.\d\d.\d\d/;
        var matcher = reg.exec(d);
        return d === '' || d === null ? -Infinity :
            parseInt((moment(matcher[0], 'D.MM.YY', null, true).format('x')), 10);
    };
}(jQuery));