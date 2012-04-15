$export("js.util.Collections");
$export("js.util.Comparator");
$export("js.util.BeanComparator");

/**
* Simple Comparator class definition
* @author Sangeeth Kumar .S
*/
js.util.Comparator = function()
{
    this.compare = Comparator_compare;
}
function Comparator_compare(left,right) {
   if (left < right) { return -1; }
   else if (left > right ) { return 1; }
   else return 0;
}

/**
* BeanComparator class definition
* @author Sangeeth Kumar .S
*/
js.util.BeanComparator = function(property,order)
{
   this.property = property;
   this.compare = BeanComparator_compare;
   this.getValue = BeanComparator_getValue;
   this.order = order?order:js.util.Collections.ASCENDING_ORDER;
}
function BeanComparator_getValue(bean)
{
    var value = bean;
    if(this.property!=null)
    {
		var props = this.property.split(".");
		for(var i = 0
			;i<props.length && value != null
			;i++)
		{
		   value = value[props[i]];
		}
	}
	
	if (value instanceof Date) {
		value = value.getTime();
	}
	
	return value;
}
function BeanComparator_compare(left,right)
{
   var state = 0;
   
   var _left = this.getValue(left);
   var _right = this.getValue(right);
   
   if ( _left < _right) { state = -1; }
   else if ( _left > _right) { state = 1; }
   else { state = 0; }
   
   return state * this.order;
}

/**
* Collections class definition
* @author Sangeeth Kumar .S
*/
function __Collections()
{
   this.defaultComparator = new js.util.Comparator();
   this.sort = __Collections_sort;
   this.ASCENDING_ORDER = 1;
   this.DESCENDING_ORDER = -1;   
}
js.util.Collections = new __Collections();

function __Collections_sort(vec,comparator,loBound, hiBound)
/**************************************************************
	This function adapted from the algorithm given in:
		Data Abstractions & Structures Using C++, by
		Mark Headington and David Riley, pg. 586.

	Quicksort is the fastest array sorting routine for
	unordered arrays.  Its big O is n log n.

    Original Code: http://www.4guysfromrolla.com/webtech/012799-1.shtml
    Modification : Comparators
 **************************************************************/
{
	if (comparator==null) comparator = this.defaultComparator;
	if (loBound == null) loBound = 0;
	if (hiBound == null) hiBound = vec.length-1;

	var pivot, loSwap, hiSwap, temp;

	// Two items to sort
	if (hiBound - loBound == 1)
	{
		if (comparator.compare(vec[loBound],vec[hiBound])>0)
		{
			temp = vec[loBound];
			vec[loBound] = vec[hiBound];
			vec[hiBound] = temp;
		}
		return;
	}

	// Three or more items to sort
	pivot = vec[parseInt((loBound + hiBound) / 2)];
	
	vec[parseInt((loBound + hiBound) / 2)] = vec[loBound];
	vec[loBound] = pivot;
	loSwap = loBound + 1;
	hiSwap = hiBound;

	do {
		// Find the right loSwap
		while (loSwap <= hiSwap && comparator.compare(vec[loSwap],pivot)<=0)
			loSwap++;

		// Find the right hiSwap
		while (comparator.compare(vec[hiSwap],pivot)>0)
			hiSwap--;

		// Swap values if loSwap is less than hiSwap
		if (loSwap < hiSwap)
		{
			temp = vec[loSwap];
			vec[loSwap] = vec[hiSwap];
			vec[hiSwap] = temp;
		}
	} while (loSwap < hiSwap);

	vec[loBound] = vec[hiSwap];
	vec[hiSwap] = pivot;


	// Recursively call function...  the beauty of quicksort

	// 2 or more items in first section		
	if (loBound < hiSwap - 1)
		this.sort(vec, comparator, loBound, hiSwap - 1);


	// 2 or more items in second section
	if (hiSwap + 1 < hiBound)
		this.sort(vec,comparator, hiSwap + 1, hiBound);
}