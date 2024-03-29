! function(t, a, e, i) {
    var n = function(a, e) {
        this.ele = a, this.defaults = {
            currentPage: 1,
            totalPage: 10,
            isShow: !0,
            count: 5,
            homePageText: "首页",
            endPageText: "尾页",
            prevPageText: "上一页",
            nextPageText: "下一页",
            callback: function() {}
        }, this.opts = t.extend({}, this.defaults, e), this.current = this.opts.currentPage, this.init()
    };
    n.prototype = {
        init: function() {
            this.isRender = !0, this.render(), this.eventBind()
        },
        render: function() {
            var t = this.opts,
                a = this.current,
                e = t.totalPage,
                i = this.getPagesTpl();
            this.homePage = '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="1">' + t.homePageText + "</a>", this.prevPage = '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + (a - 1) + '">' + t.prevPageText + "</a>", this.nextPage = '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + (a + 1) + '">' + t.nextPageText + "</a>", this.endPage = '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + e + '">' + t.endPageText + "</a>", this.checkPage(), this.isRender && this.ele.html("<div class='ui-pagination-container'>" + this.homePage + this.prevPage + i + this.nextPage + this.endPage + "</div>")
        },
        checkPage: function() {
            var t = this.opts,
                a = t.totalPage,
                e = this.current;
            t.isShow || (this.homePage = this.endPage = ""), 1 === e && (this.homePage = this.prevPage = ""), e === a && (this.endPage = this.nextPage = ""), 1 === a && (this.homePage = this.prevPage = this.endPage = this.nextPage = ""), a <= 1 && (this.isRender = !1)
        },
        getPagesTpl: function() {
            var t = this.opts,
                a = t.totalPage,
                e = this.current,
                i = "",
                n = t.count;
            if (a <= n)
                for (g = 1; g <= a; g++) i += g === e ? '<a href="javascript:void(0);" class="ui-pagination-page-item active" data-current="' + g + '">' + g + "</a>" : '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a>";
            else {
                var s = n / 2;
                if (e <= s)
                    for (g = 1; g <= n; g++) i += g === e ? '<a href="javascript:void(0);" class="ui-pagination-page-item active" data-current="' + g + '">' + g + "</a>" : '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a>";
                else {
                    var r = Math.floor(s),
                        c = e + r,
                        h = e - r,
                        o = n % 2 == 0;
                    c > a && (o ? (h -= c - a - 1, c = a + 1) : (h -= c - a, c = a)), o || c++;
                    for (var g = h; g < c; g++) i += g === e ? '<a href="javascript:void(0);" class="ui-pagination-page-item active" data-current="' + g + '">' + g + "</a>" : '<a href="javascript:void(0);" class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a>"
                }
            }
            return i
        },
        eventBind: function() {
            var a = this,
                e = this.opts.callback;
            this.ele.on("click", ".ui-pagination-page-item", function() {
                var i = t(this).data("current");
                a.current != i && (a.current = i, a.render(), e && "function" == typeof e && e(i))
            })
        }
    }, t.fn.pagination = function(t) {
        return new n(this, t), this
    }
}(jQuery, window, document);