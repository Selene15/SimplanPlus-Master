package it.unibo.simpLan;

import AST.ImplSimpLanPlusVisitor;
import AST.Node;
import Utils.EffectsSymTable;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;
import Utils.SymTable;
import Utils.TypeExp;
import Utils.TypeValue;
import gen.SimpLanPlusLexer;
import gen.SimpLanPlusParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    Node fileOpening(String num) throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {

        FileInputStream fs = new FileInputStream("Test/test" + num + ".plan");
        ANTLRInputStream inputStream = new ANTLRInputStream(fs);
        SimpLanPlusLexer lexer = new SimpLanPlusLexer(inputStream);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        SimpLanPlusParser parser = new SimpLanPlusParser(stream);

        ImplSimpLanPlusVisitor visitor = new ImplSimpLanPlusVisitor();

        Node ast = visitor.visit(parser.statement());
        return ast;
    }

    @Test
    void typeCheckFileFiboncci() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file Fibonacci: Integer -- Fibonacci");
        SymTable sm = new SymTable();
        Node n = fileOpening("-fibonacci");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFileFattoriale() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file Fattoriale: Integer -- Fattoriale");


        SymTable sm = new SymTable();
        Node n = fileOpening("-fattoriale");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile1() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 1: Test prof");

        SymTable sm = new SymTable();
        Node n = fileOpening("1");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    void typeCheckFile2() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        System.out.println("TypeCheck file 2: Test prof");
        Assertions.assertThrows(IdNotDefinedException.class, () -> {
            Node ast = fileOpening("2");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile3() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 3: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("3");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile4() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 4: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("4");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile5() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 5: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("1");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile6() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 6: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("6");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile7() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 7: Test prof");
        Assertions.assertThrows(TypeCheckingException.class, () -> {
            Node ast = fileOpening("7");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile8() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 8: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("8");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile9() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 9: Test prof");
        SymTable sm = new SymTable();
        Node n = fileOpening("9");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile10() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 10: Void -- Testcase Print stm");
        SymTable sm = new SymTable();
        Node n = fileOpening("10");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile11() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 11: Void -- Testcase Deletion variable");
        Assertions.assertThrows(TypeCheckingException.class, () -> {
            Node ast = fileOpening("11");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile12() throws IOException, TypeCheckingException {
        System.out.println("TypeCheck file 12: Error -- Testcase in exp");
        Assertions.assertThrows(TypeCheckingException.class, () -> {
            Node ast = fileOpening("12");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile13() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 13: Int -- Testcase Ite stm");
        SymTable sm = new SymTable();
        Node n = fileOpening("13");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }


    @Test
    void typeCheckFile14() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 14: Ref Int -- Testcase New exp");
        SymTable sm = new SymTable();
        Node n = fileOpening("14");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.POINTER, new TypeExp(TypeValue.INTEGER)));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile15() throws IOException, TypeCheckingException, IdAlreadyDefinedException {
        System.out.println("TypeCheck file 15: Void -- Testcase Error Assgn stm");
        Assertions.assertThrows(IdNotDefinedException.class, () -> {
            Node ast = fileOpening("15");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile17() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        System.out.println("TypeCheck file 17: Ref Int -- Testcase Dichiarazione di funzione");
        SymTable sm = new SymTable();
        Node n = fileOpening("17");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }


    @Test
    void typeCheckFile19() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("TypeCheck file 19: Integer -- Fun Call Integer");
        SymTable sm = new SymTable();
        Node n = fileOpening("19");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile20() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("TypeCheck file 20: Test IF");
        SymTable sm = new SymTable();
        Assertions.assertThrows(EffectsException.class, () -> {
            Node ast = fileOpening("20");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile21() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("TypeCheck file 21: Pointer deleted -- Assignment");
        SymTable sm = new SymTable();
        Assertions.assertThrows(EffectsException.class, () -> {
            Node ast = fileOpening("21");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile22() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("TypeCheck file 22: Test function");
        SymTable sm = new SymTable();
        Node n = fileOpening("22");
        assertEquals(n.checkType(sm), new TypeExp(TypeValue.VOID));
        EffectsSymTable es = new EffectsSymTable();
        n.checkEffects(es);
    }

    @Test
    void typeCheckFile23() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("TypeCheck file 23: Test function");
        Assertions.assertThrows(EffectsException.class, () -> {
            Node ast = fileOpening("23");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile26() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("CheckEffects file 26: Test function");
        Assertions.assertThrows(IdNotDefinedException.class, () -> {
            Node ast = fileOpening("26");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }

    @Test
    void typeCheckFile27() throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException,EffectsException {
        System.out.println("CheckEffects file 27: Test function");
        Assertions.assertThrows(EffectsException.class, () -> {
            Node ast = fileOpening("27");
            ast.checkType(new SymTable());
            ast.checkEffects(new EffectsSymTable());
        });
    }
}
