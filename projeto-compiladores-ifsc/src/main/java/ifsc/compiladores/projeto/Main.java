package ifsc.compiladores.projeto;

import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.generator.LLVMIRGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar.ProgramaContext;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine;


import javax.swing.*;

@CommandLine.Command(
        name="ParseCommand",
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        description = "Compile the executable to LLVM IR"
)
public class Main implements Callable<Integer> {

    @CommandLine.Option(names = {"-t", "--showTree"}, description = "Display the parse tree")
    private boolean showTree;

    @Override
    public Integer call() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            StringBuilder inputBuilder = new StringBuilder();
//            System.out.println("Enter your code (type '--stop' to stop and compile the input or '--exit' to exit the program): ");
//            while (true) {
//                String line = scanner.nextLine();
//
//                if (line.trim().equalsIgnoreCase("--exit")) {
//                    return 0;
//                }
//
//                if (line.trim().equalsIgnoreCase("--stop")) {
//                    break;
//                }
//
//                inputBuilder.append(line).append(System.lineSeparator());
//            }

            inputBuilder.append("""
                    int test() {
                        int a, b;
                        
                        a = 10;
                        b = 5;
                        b = a;
                    }
                    """);

            String input = inputBuilder.toString().trim();
            if (input.isEmpty()) {
                System.out.println("No input detected. Exiting...");
                return 0;
            }

            LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(input));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            ParserGrammar parser = new ParserGrammar(tokenStream);

            ProgramaContext programaContext = parser.programa();

            LLVMIRGeneratorVisitor visitor = new LLVMIRGeneratorVisitor();

            Fragment program = visitor.visitPrograma(programaContext);

            System.out.println(program.getText());

            if (showTree) {
                showTree(parser, programaContext);
            }

            scanner.next();
        }
    }

    private static void showTree(ParserGrammar parser, ParseTree tree) {
        JFrame frame = new JFrame("Antlr Parser Tree");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), tree
        );

        viewer.setScale(1.5);
        panel.add(viewer);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        // Centralizar o JFrame na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - frame.getWidth()) / 2;
        int centerY = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(centerX, centerY);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}