Pxsum : ListPattern {
	var <>sum, <>minval;
	*new { arg list, sum=1, repeats=1, minval=0.001;
		^super.new(list, repeats)
		.sum_(sum)
		.minval_(minval);
	}

	embedInStream { arg inval;
		var item;
		var localList = list.copy.xsum(sum, minval);

		repeats.value(inval).do({
			localList.size.do({ |i|
				item = localList.wrapAt(i);
				inval = item.embedInStream(inval);
			});
		});
		^inval;
	}
}