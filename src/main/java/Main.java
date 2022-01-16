import AST.ImplSimpLanPlusVisitor;
import AST.Node;
import Interpreter.ExecuteVM;
import Interpreter.ParserInterpreter.SVMLexer;
import Interpreter.ParserInterpreter.SVMParser;
import Interpreter.ParserInterpreter.SVMVisitorImpl;
import Utils.EffectsSymTable;
import Utils.OffsetSymTable;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;
import Utils.SymTable;
import gen.SimpLanPlusLexer;
import gen.SimpLanPlusParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException, TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException, EffectsException {
        // Preparazione delll'input di ANTLR
        FileInputStream fs = new FileInputStream("Test/test1.plan");
        ANTLRInputStream inputStream = new ANTLRInputStream(fs);SimpLanPlusLexer lexer = new SimpLanPlusLexer(inputStream);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        SimpLanPlusParser parser= new SimpLanPlusParser(stream);
        // Costruizione del vistor
        ImplSimpLanPlusVisitor visitor = new ImplSimpLanPlusVisitor();
        Node ast = visitor.visit(parser.block());
        // Stampa dell'albero
        System.out.println(ast.toPrint("-"));
        //Type Checking
        ast.checkType(new SymTable());
        // Analisi degli effetti
        ast.checkEffects(new EffectsSymTable());
        // Generazione del codice
        System.out.println(ast.generateCode(new OffsetSymTable()));
        // Preparazione dell'input per la VM
        InputStream streamSVM = new ByteArrayInputStream(ast.generateCode(new OffsetSymTable()).getBytes(StandardCharsets.UTF_8));
        SVMLexer svmLexer = new SVMLexer(CharStreams.fromStream(streamSVM, StandardCharsets.UTF_8));
        CommonTokenStream tokensASM = new CommonTokenStream(svmLexer);
        SVMParser svmParser = new SVMParser(tokensASM);
        SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
        visitorSVM.visit(svmParser.assembly());
        System.out.println("You had: "+svmLexer.lexicalErrors+" lexical errors and "+svmParser.getNumberOfSyntaxErrors()+" syntax errors.");
        if (svmLexer.lexicalErrors>0 || svmParser.getNumberOfSyntaxErrors()>0) System.exit(1);
        // Esecuzione
        System.out.println("Starting Virtual Machine...");
        ExecuteVM vm = new ExecuteVM(visitorSVM.code);
        vm.cpu();
        System.out.println("Finishing Virtual Machine...");
    }
}
