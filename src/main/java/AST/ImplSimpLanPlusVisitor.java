package AST;



import java.util.ArrayList;
import java.util.List;

import AST.ImplNode.*;
import Utils.TypeExp;
import Utils.TypeValue;
import gen.SimpLanPlusBaseVisitor;
import gen.SimpLanPlusParser;
import gen.SimpLanPlusParser.ArgContext;
import gen.SimpLanPlusParser.DeclarationContext;
import gen.SimpLanPlusParser.StatementContext;

public class ImplSimpLanPlusVisitor extends SimpLanPlusBaseVisitor<Node> {
    // Visitor del block
    @Override
    public Node visitBlock(SimpLanPlusParser.BlockContext ctx) {
    	List<Node> declist = new ArrayList<>();
    	List<Node> stlist = new ArrayList<>();
        // Dichiarazioni
    	for(DeclarationContext d : ctx.declaration()) {
    		declist.add(visit(d));
    	}
        // Statement
    	for(StatementContext s : ctx.statement()) {
    			stlist.add(visit(s));
    	}
        return new BlockNode(declist,stlist);
    }
    // Visitor dello statement
    @Override
    public Node visitStatement(SimpLanPlusParser.StatementContext ctx) {
        Node stm = null;
        // Valuto il tipo di statement e uso il costruttore adatto
        if(ctx.assignment() != null){
        	stm = new StatementNode(visit(ctx.assignment()), StatementNode.Type.assignment);
        }
        if(ctx.deletion() != null){
            stm = new StatementNode(visit(ctx.deletion()), StatementNode.Type.deletion);

        }
        if(ctx.block() != null){
            stm = new StatementNode(visit(ctx.block()), StatementNode.Type.block);

        }
        if(ctx.print() != null){
            stm = new StatementNode(visit(ctx.print()), StatementNode.Type.print);

        }
        if(ctx.ret() != null){
            stm = new StatementNode(visit(ctx.ret()), StatementNode.Type.ret);

        }
        if(ctx.ite() != null){
            stm = new StatementNode(visit(ctx.ite()), StatementNode.Type.ite);
        }
        if(ctx.call() != null){
            stm = new StatementNode(visit(ctx.call()), StatementNode.Type.call);
        }
        return stm;
    }
    // Visitor della dichiarazione
    @Override
    public Node visitDeclaration(SimpLanPlusParser.DeclarationContext ctx) {
        Node node = null;
        // Se è una funzione
    	if(ctx.decFun() != null){
            node = new DecNode(visit(ctx.decFun()),DecNode.Type.decFun);
        }
    	// Se è una dichiarazione di variabile
    	if(ctx.decVar()!=null) {
    		node = new DecNode(visit(ctx.decVar()),DecNode.Type.decVar);
    	}
        return node;
    }
    // Visitor dichiarazione di funzione
    @Override
    public Node visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
    	List<Node> arglist = new ArrayList<>();
        // Aggiunge gli argomenti
    	for(ArgContext a : ctx.arg()) {
    		arglist.add(visit(a));
    	}
    	// Se non è void il tipo della funzione
        if(ctx.type()==null) {
            return new DecFunNode(new TypeNode(new TypeExp(TypeValue.VOID)),ctx.ID().getText(),arglist,visit(ctx.block()) );
    	}
    	
        return new DecFunNode(visit(ctx.type()), ctx.ID().getText(),arglist,visit(ctx.block()) );
    }
    // Visitor dichiarazione di var
    @Override
    public Node visitDecVar(SimpLanPlusParser.DecVarContext ctx) {
        Node node;
    	if(ctx.exp()==null) {
    		node = new DecVarNode(ctx.ID().getText(), visit(ctx.type()));
    	}
    	else {
    		node = new DecVarNode(ctx.ID().getText(), visit(ctx.type()), visit(ctx.exp()));
    	}
        return node;
    }
    // Visitor della dichiarazione di tipo
    @Override
    public Node visitType(SimpLanPlusParser.TypeContext ctx) {

        if (ctx.getText().equals("int")) {
            return new TypeNode(new TypeExp(TypeValue.INTEGER));
        }
        if (ctx.getText().equals("bool")) {
            return new TypeNode(new TypeExp(TypeValue.BOOL));
        }

        TypeNode t = (TypeNode) visit(ctx.type());
        return new TypeNode(new TypeExp(TypeValue.POINTER, t.getPtr_type()));
    }
    // Visitor dell'argomento di funzione
    @Override
    public Node visitArg(SimpLanPlusParser.ArgContext ctx) {
        return new ArgNode(visit(ctx.type()),ctx.ID().getText());
    }
    // Visitor dell'assegnamento
    @Override
    public Node visitAssignment(SimpLanPlusParser.AssignmentContext ctx) {
        return new AssignmentNode(visit(ctx.lhs()), visit(ctx.exp()));
    }
    // Visitor dell'lhsNode
    @Override
    public Node visitLhs(SimpLanPlusParser.LhsContext ctx) {
        Node node;
        // Se puntatore
        if(ctx.lhs() == null){
            node = new LhsNode(ctx.ID().getText());
        } else {
            // Se id
            node = new LhsNode(visit(ctx.lhs()));
        }
        return node;
    }
    // Visitor Delete
    @Override
    public Node visitDeletion(SimpLanPlusParser.DeletionContext ctx) {
    	return new DeletionNode(ctx.ID().getText());
    }
    // Visitor Print
    @Override
    public Node visitPrint(SimpLanPlusParser.PrintContext ctx) {
        return new PrintNode(visit(ctx.exp()));
    }
    // Visitor Return
    @Override
    public Node visitRet(SimpLanPlusParser.RetContext ctx) {
        if(ctx.exp() == null){
            return new RetNode(new ExpNode(ExpNode.Type.voidExp));
        }
       return new RetNode(visit(ctx.exp()));
    }
    // Visitor ifNode
    @Override
    public Node visitIte(SimpLanPlusParser.IteContext ctx) {
        return new IteNode(visit(ctx.exp()), visit(ctx.statement(0)), ctx.statement().size() == 2 ? visit(ctx.statement(1))  : null);
    }
    // Visitor CallNode
    @Override
    public Node visitCall(SimpLanPlusParser.CallContext ctx) {
        ArrayList<ExpNode> expNodes = new ArrayList<>();
        for (SimpLanPlusParser.ExpContext e: ctx.exp())
            expNodes.add((ExpNode) visit(e));
        return new CallNode(ctx.ID().getText(), expNodes);
    }
    // Visitor (e)
    @Override
    public Node visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) {
        return new ExpNode(ExpNode.Type.baseExp, visit(ctx.exp()));
    }
    //Visitor delle espressioni binarie
    @Override
    public Node visitBinExp(SimpLanPlusParser.BinExpContext ctx) {
        return new ExpNode(
                ExpNode.Type.binExp,
                visit(ctx.exp().get(0)),
                visit(ctx.exp().get(1)),
                ctx.op.getText()
        );
    }
    // Visitor espressione che contiente lhsNode
    @Override
    public Node visitDerExp(SimpLanPlusParser.DerExpContext ctx) {
        return new ExpNode(ExpNode.Type.derExp, (LhsNode) visit(ctx.lhs()));
    }
    // Visitor new T
    @Override
    public Node visitNewExp(SimpLanPlusParser.NewExpContext ctx) {
        return new ExpNode(ExpNode.Type.newExp, ((TypeNode) visit(ctx.type())).getPtr_type());
    }
    // Visitor valore numerico
    @Override
    public Node visitValExp(SimpLanPlusParser.ValExpContext ctx) {
        return new ExpNode(ExpNode.Type.valExp, Integer.parseInt(ctx.getText()));
    }
    // Visitor -e
    @Override
    public Node visitNegExp(SimpLanPlusParser.NegExpContext ctx) {
        return new ExpNode(ExpNode.Type.negExp, visit(ctx.exp()));
    }
    // Visitor (true|false)
    @Override
    public Node visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) {
        return new ExpNode(ExpNode.Type.boolExp, Boolean.valueOf(ctx.BOOL().getText()));
    }
    // Visitor f(x1,...,xn)
    @Override
    public Node visitCallExp(SimpLanPlusParser.CallExpContext ctx) {
        return new ExpNode(ExpNode.Type.callExp, (CallNode) visit(ctx.call()));
    }
    // Visitor !e
    @Override
    public Node visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
        return new ExpNode(ExpNode.Type.notExp, visit(ctx.exp()));
    }
}

