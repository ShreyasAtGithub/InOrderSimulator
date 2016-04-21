# InOrderSimulator
This is an application which imitates the basic operations of a typical processor, which accepts instructions 
as inputs and processes them passing them 5 stages, namely Fetch, Decode, Execute, Memory and Writeback. 

Memory dependencies and Data dependencies are handled and stalls(bubbles) have been taken care of while 
issuing instructions to execute.  It also handles the special cases apart from LOAD/STORE instructions: 
BAL    : Branch after Link 
JUMP   : Typical Branching 
BZ     : Branch if zero
BNZ    : Branch if not zero
