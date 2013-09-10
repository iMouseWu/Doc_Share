function paging(){
	this.fn = null;
	this.pre = "<";
	this.next = ">";
	this.pagenav = pagenav;
	function pagenav(a,b){
		var tabsize = 5;  //9;/*分页栏显示的页码的数目*/
	var besidesize = 2;      //4;/*两边的页码数*/
	var whenmove =  4 ;      //7;/*到第几个的时候开始移动*/
	
	/*whenmove - besidesize >= 2
	 * 这个蛋疼的东西可以用数学推导：因为由1处得已经把1这个输出了，再结合2处，所以d>1（这里由d>0可知whenmove > besidesize）;
	 * a-besidesize>1.由3处可得当whenmove<=a的时候 a-besidesize需要大于1；
	 * 因为a是越来越大的，所以a=whenmove的时候需要a-besidesize需要大于1，所以whenmove - besidesize >= 2；
	 * 
	 * 这个后续会继续完善的
	 * */
	if(1>=b)return this.pn=this.p=1,pHtml2(1);
	b<a&&(a=b);
	1>=a?a=1:(pHtml(a-1,b,this.pre),pHtml(1,b,"1"));/*1*/
	this.p=a;
	this.pn=b;
	var d=2,e=tabsize>b?b:tabsize;
	whenmove<=a&&($("#pagenumber").append("<li><span>...<span></>"),d=a-besidesize,e=a+besidesize,e=b<e?b:e);/*3*/
	for(;d<a;d++)pHtml(d,b);/*2*/
	pHtml2(a);
	for(d=a+1;d<=e;d++) pHtml(d,b);
	e<b&&($("#pagenumber").append("<li><span>...<span></li>"),pHtml(b,b));
	a<b&&(pHtml(a+1,b,this.next));
	}
	this.go = go;
	function go(a,b){
		$('#pagenumber').html("");
		this.pagenav(a,b);
		null!=this.fn&&this.fn(this.p,this.pn)
	}

}