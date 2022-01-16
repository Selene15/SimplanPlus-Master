package Interpreter;

import Interpreter.ParserInterpreter.SVMParser;

public class ExecuteVM {
    
    public static final int CODESIZE = 10000;
    //public static final int MEMSIZE = 10000;

    public static final int MEMSIZE = 10000;

    private final int[] code;
    private final int[] memory = new int[MEMSIZE];

    private int ip = 0;
    private int sp = MEMSIZE;
    private int hp = 0;
    private int fp = MEMSIZE - 1;
    private int ra = 0;
    private int rv;
    
    public ExecuteVM(int[] code) {
      this.code = code;
    }

    public void cpu() {
      while (ip < CODESIZE) {
    	if(hp+1>=sp) {
    		System.out.println("\nError: Out of memory");
            return;
    	}
    	else {
    		int bytecode = code[ip++]; // fetch
            int v1,v2;
            int address;
            switch (bytecode) {
                case SVMParser.PUSH ->
                        push(code[ip++]);
                case SVMParser.POP ->
                        pop();
                case SVMParser.ADD -> {
                    v1 = pop();
                    v2 = pop();
                    push(v2 + v1);
                }
                case SVMParser.MULT -> {
                    v1 = pop();
                    v2 = pop();
                    push(v2 * v1);
                }
                case SVMParser.DIV -> {
                    v1 = pop();
                    v2 = pop();
                    push(v2 / v1);
                }
                case SVMParser.SUB -> {
                    v1 = pop();
                    v2 = pop();
                    push(v2 - v1);
                }
                case SVMParser.AND -> {
                    boolean vb1 = (pop() != 0 );
                    boolean vb2 = (pop() != 0);
                    push(vb2 && vb1 ? 1 : 0);
                }
                case SVMParser.OR -> {
                    boolean vb1 = (pop() != 0 );
                    boolean vb2 = (pop() != 0);
                    push(vb2 || vb1 ? 1 : 0);
                }

                case SVMParser.NOT -> {
                    boolean vb1 = (pop() != 0 );
                    push(vb1 ? 0 : 1);
                }
                case SVMParser.STOREW -> { //
                    address = pop();
                    memory[address] = pop();
                }
                case SVMParser.LOADW -> { //
                    // check if object address where we take the method label
                    // is null value (-10000)
                    if (memory[sp] == -10000) {
                        System.out.println("\nError: Null pointer exception");
                        return;
                    }
                    push(memory[pop()]);
                }
                case SVMParser.BRANCH -> {
                    address = code[ip];
                    ip = address;
                }
                case SVMParser.BRANCHFUN -> {
                    address = code[ip++];
                    ra = ip;
                    ip = address;
                }
                case SVMParser.BRANCHEQ -> { //
                    address = code[ip++];
                    v1 = pop();
                    v2 = pop();
                    if (v2 == v1) ip = address;
                }
                case SVMParser.BRANCHNEQ -> {
                    address = code[ip++];
                    v1 = pop();
                    v2 = pop();
                    if (v2 != v1) ip = address;
                }
                case SVMParser.EQ -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 == v1) push(1);
                    else push(0);
                }
                case SVMParser.NEQ -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 != v1) push(1);
                    else push(0);
                }
                case SVMParser.LESSEQ -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 <= v1) push(1);
                    else push(0);
                }
                case SVMParser.LESSOP -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 < v1) push(1);
                    else push(0);
                }
                case SVMParser.MOREOP -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 > v1) push(1);
                    else push(0);
                }
                case SVMParser.MOREEQ -> {
                    v1 = pop();
                    v2 = pop();
                    if (v2 >= v1) push(1);
                    else push(0);
                }
                case SVMParser.JS -> { //
                    address = pop();
                    ra = ip;
                    ip = address;
                }
                case SVMParser.STORERA -> //
                        push(ra);
                case SVMParser.LOADRA -> //
                        ra = pop();
                 case SVMParser.STORERV -> //
                        push(rv);
                case SVMParser.LOADRV -> //
                        rv = pop();
                case SVMParser.LOADFP -> //
                        fp = pop();
                case SVMParser.STOREFP -> //
                        push(fp);
                case SVMParser.COPYFP -> //
                        fp = sp;
                case SVMParser.STOREHP -> //
                        hp = pop();
                case SVMParser.LOADHP -> //
                        push(hp++);
                case SVMParser.PRINT -> System.out.println((sp < MEMSIZE) ? memory[sp] : "Empty stack!");
                case SVMParser.HALT -> {
                    //to print the result
                    System.out.println("\nResult: " + memory[sp] + "\n");
                    return;
                }
            }
    	} 
      }
    } 
    
    private int pop() {
        int tmp = memory[sp];
        memory[sp] = 0;
        sp++;
        return tmp;
    }
    
    private void push(int v) {
      memory[--sp] = v;
    }
    
}