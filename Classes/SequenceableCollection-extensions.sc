+ SequenceableCollection {
	xsum { |sum, minval=0.001|
		var out = List(this.size);
		var iterator = Pshuf(this).repeat.asStream;
		var runningTotal = 0;
		var space, candidate;
		//var numSkips = 0;

		while (
			{ (sum - runningTotal) > minval },
			{
				space = sum - runningTotal;
				candidate = iterator.next;
				if (candidate <= space,
					{
						out.add(candidate);
						runningTotal = runningTotal + candidate;
					},
					{
						// numSkips = numSkips + 1;
						/* if the amount of remaining space is in the
						   array's lower quartile, we finish by adding
						   that amount of space as the final element  */

						(space < this.q1).if({
							(space < minval).if({
								out[out.size - 1] = out.last + space;
								runningTotal = sum; // Force exact sum
							},{
								out.add(space);
								runningTotal = sum; // Force exact sum
							});
						});
					}
				);
			}
		);

		// Final correction: ensure we hit the target exactly
		(out.size > 0).if({
			var actualSum = out.sum{|i|i.value};
			var correction = sum - actualSum;
			(correction.abs > 1e-10).if({  // If there's a meaningful difference
				out[out.size - 1] = out.last + correction;
			});
		});

		//"skipRatio: %".format(numSkips/out.size).postln;
		^out.asArray;
	}

	prQuartile { |position|
		var sorted = this.copy.sort;
		var len = this.size;
		^len.even.if(
			{ [sorted[len * position - 1], sorted[len * position]].mean },
			{ sorted[(len * position).floor] }
		);
	}

	q1 {
		^this.prQuartile(0.25);
	}

	q2 {
		^this.prQuartile(0.5);
	}

	q3 {
		^this.prQuartile(0.75);
	}
}