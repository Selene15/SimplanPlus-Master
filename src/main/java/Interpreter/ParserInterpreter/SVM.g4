grammar SVM;

@header {
import java.util.HashMap;
}

@lexer::members {
public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
  
assembly: (instruction)* ;

instruction:
    ( PUSH n=NUMBER 
	  | PUSH l=LABEL 		     
	  | POP		    
	  | ADD		    
	  | SUB		    
	  | MULT	    
	  | DIV
	  | STOREW	  
	  | LOADW           
	  | l=LABEL COL     
	  | BRANCH l=LABEL  
	  | BRANCHEQ l=LABEL
	  | BRANCHNEQ l=LABEL
	  | LESSEQ
	  | MOREEQ
	  | LESSOP
	  | MOREOP
	  | EQ
	  | NEQ
	  | BRANCHFUN l=LABEL
	  | JS              
	  | LOADRA          
	  | STORERA         
	  | LOADRV          
	  | STORERV         
	  | LOADFP          
	  | STOREFP         
	  | COPYFP          
	  | LOADHP          
	  | STOREHP         
	  | PRINT
	  | AND
	  | OR
	  | NOT
	  | HALT
	  ) ;
 	 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
 
PUSH : 'push' ;         // pushes constant in the stack
POP	 : 'pop' ;      	// pops from stack
ADD	 : 'add' ;          // add two values from the stack
SUB	 : 'sub' ;	        // add two values from the stack
MULT : 'mult' ;         // add two values from the stack
DIV	 : 'div' ;	        // add two values from the stack
AND : 'and' ;           // && of two values from the stack
OR : 'or' ;             // || of two values from the stack
NOT : 'not' ;           // change value of top element from 1 to 0 (and viceversa)
STOREW	 : 'sw' ; 	    // store in the memory cell pointed by top the value next
LOADW	 : 'lw' ;	    // load a value from the memory cell pointed by top
BRANCH	 : 'b' ;	    // jump to label
BRANCHEQ : 'beq' ;	    // jump to label if top == next
BRANCHNEQ : 'bneq' ;    // jump to label if top != next
LESSEQ:'lesseq' ;	    // jump to label if top <= next
LESSOP: 'less' ;        // jump to label if top < next
MOREOP: 'more' ;        // jump to label if top > next
MOREEQ:'moreq' ;	    // jump to label if top >= next
EQ:'eq';
NEQ:'neq';
JS	 : 'js' ;	        // jump to instruction pointed by top of stack and store next instruction in ra
LOADRA	 : 'lra' ;	    // load from ra
STORERA  : 'sra' ;      // store top into ra
STORERV	 : 'srv' ;	    // store top from rv
LOADRV  : 'lrv' ;	    // store top into rv
LOADFP	 : 'lfp' ;	    // store top into frame pointer
STOREFP	 : 'sfp' ;	    // load frame pointer in the stack
COPYFP   : 'cfp' ;      // copy stack pointer into frame pointer
LOADHP	 : 'lhp' ;	    // load heap pointer in the stack
STOREHP	 : 'shp' ;	    // store top into heap pointer
PRINT	 : 'print' ;	// print top of stack
HALT	 : 'halt' ;	    // stop execution
BRANCHFUN : 'bf' ;      //branch for set ra

COL	 : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);

ERR   	 : . { System.err.println("Invalid char: "+ getText()); lexicalErrors++;  } -> channel(HIDDEN); 

