/*
 * Author: Lucas Ryan 
 * FileName: Maze.java
 * Date: July 31, 2016
 * NID: lu469191
 */
public class RunLikeHell {
	
	// We update the values in our blocks array to store the maximum
	// value we can get jumping to that block.  The one thing we must
	// keep in mind is that we can't jump the immediately previous block.
	// Runtime: O(n), Assume: we aren't given an empty array.
	public static int maxGain(int [] blocks) {
		// if null/empty array nothing to hit then
		if(blocks == null || blocks.length == 0) return 0;
		
		// if we only have a size of 1 then just return it's value
		if(blocks.length == 1) return blocks[0];

		// have index pointer for the block with the maxValue we have hit 
		int maxValIndex = (blocks[0] < blocks[1]) ? 1:0;
		// have index pointer for the block with the max value that we are 
		// guaranteed to have been able to hit if we hit this block
		int tempMaxValIndex = 0;
		// counter to go through array
		int i;
		// iterate through array, updating each cell with the max possible value we can get if we hit the box
		for(i = 2; i < blocks.length; i++) {
			// update value to be, value in block + value at block that has the max value we are able to hit
			blocks[i] = Math.max(blocks[i], blocks[i] + blocks[tempMaxValIndex]);
			// update the pointer that keep tracks of block with max value
			maxValIndex = (blocks[maxValIndex] <  blocks[tempMaxValIndex]) ? tempMaxValIndex : maxValIndex;
			// update are temp pointer of the 2nd max value if the box we previously setup is the 2nd greatest value
			tempMaxValIndex = (blocks[i-1] <  blocks[tempMaxValIndex]) ? tempMaxValIndex : (i-1);	
		}
		// need to do one more update, to save value for the last cell in the blocks array
		tempMaxValIndex = (blocks[i-1] <  blocks[tempMaxValIndex]) ? tempMaxValIndex : (i-1);
		maxValIndex = (blocks[maxValIndex] <  blocks[tempMaxValIndex]) ? tempMaxValIndex : maxValIndex;
		
		return (blocks[maxValIndex]);
	}
}
