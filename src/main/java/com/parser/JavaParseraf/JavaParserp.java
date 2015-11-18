package com.parser.JavaParseraf;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaParserp {

	private ArrayList<String> interfaceNames = new ArrayList<String>();
	private ArrayList<String> ClassListNames = new ArrayList<String>();
	
	public int value;
	public String classNames = "";
	private String[] dataTypes = { "byte", "short", "int", "long", "float", "double", "boolean", "char", "Byte",
			"Short", "Int", "Long", "Float", "Double", "Boolean", "Char" };

    //private static String javaFileLocation ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-2/uml-parser-test-2";
	
	//private static String javaFileLocation ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-3/uml-parser-test-3";

    //private static String javaFileLocation ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-4/uml-parser-test-4";
    private static String javaFileLocation = "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1 (1)/uml-parser-test-1";

	/**
	 * /Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1
	 * (1)/uml-parser-test-1
	 **/
	/**
	 * //Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-2/uml-parser-test-2
	 **/

	public static void main(String[] args) {
		JavaParserp up = new JavaParserp();
		up.parseInputFile();
	}

	public static class MethodVisitor extends VoidVisitorAdapter {

		@Override
		public void visit(MethodDeclaration n, Object arg) {
			// here you can access the attributes of the method.
			// this method will be called for all methods in this
			// CompilationUnit, including inner class methods
			System.out.println(n.getName());
		}
	}

	public void parseInputFile() {

		/**********
		 * Set the folder from which we need to get the .java files
		 ******************/
		File location = new File(javaFileLocation);
		try {
			/**********
			 * Read Each JAVA file in the folder iteratively
			 ******************/

			for (File javaFile : location.listFiles(javaFiles)) {

				FileInputStream inputStream = new FileInputStream(javaFile.getAbsolutePath());

				/*****
				 * Compilation Unit is the MainCrux String int project
				 ******/
				CompilationUnit cu = JavaParser.parse(inputStream);

				/*****
				 * Node List Creation for Storing the Parsed Nodes of the Code
				 ******/

				List<Node> cuChildNodes = cu.getChildrenNodes();
				List<TypeDeclaration> listOfTypeDeclarations = cu.getTypes();

				/*****
				 * Iterate over the Nodes and Printign the Nodes in the list
				 ******/

				String outputString = "[";
				String outputInterface = "";
				String outputImplements = "";
				String outputMethodDec="";

				for (Node cuChildNode : cuChildNodes) {

					if (cuChildNode instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) cuChildNode;

						if (cid.toString().contains("interface")) {
							interfaceNames.add(cid.getName());

						} else {

							classNames = cid.getName();

							if (cid.getExtends() != null) {
								for (Node Extends : cid.getExtends()) {
									outputImplements += "[" + Extends + "]^-[" + cid.getName() + "],";

								}
							}

							if (cid.getImplements() != null) {
								for (Node Implements : cid.getImplements()) {
        
									 outputInterface += "[<<interface>>;" + Implements + "]^-.-[" + cid.getName() + "],";
								}
							}
						}
					}
				}

				outputString = outputString + classNames + "|";

				for (TypeDeclaration listOfTypeDeclaration : listOfTypeDeclarations) {

					List<BodyDeclaration> listOfBodyDeclarations = listOfTypeDeclaration.getMembers();

					String typeString = "";
					String submitFieldReferences = "";
					String methodType="";
					String methodDecVar="";
					String methodDec="";

					for (int i = 0; i < listOfBodyDeclarations.size(); i++) {
						BodyDeclaration bd = listOfBodyDeclarations.get(i);

     					String submitURLModifier = "";
						
     				
						if (bd instanceof FieldDeclaration) {
							System.out.println("Field Declaration"+bd);
							
							FieldDeclaration fd = (FieldDeclaration) bd;
							int modifiers = fd.getModifiers();
							switch (modifiers) {
							case ModifierSet.PRIVATE:
								submitURLModifier = "-"; // Variable is private;
								break;
							case ModifierSet.PUBLIC:
								submitURLModifier = "+"; // Variable is public
								break;
							case ModifierSet.PROTECTED:
								submitURLModifier = "~"; // Variable is
															// protected
								break;
							}

							List<Node> listNodes = fd.getChildrenNodes();

							/*****
							 * Defining the datatypes of the individual nodes
							 *******/
							String outputRefString = "";
							String outputRefvariable = "";
							String outputReftype = "";

							boolean primFlag = false;

							for (Node refTypeNode : listNodes) {

								// System.out.println(refTypeNode);// All are
								// here

								if (refTypeNode instanceof ReferenceType) {
									ReferenceType refType = (ReferenceType) refTypeNode;

									// The reference Type can be
									// primitive/nonprimitive/variable
									// case 1:

									boolean primitiveMatch;

									String strGenericsObject = "";

									for (String str : dataTypes) {
										if (refType.toString().contains(str)) {

											outputReftype = refType.getType().toString() + "(*)";

											primFlag = true;
										}

									}

									if (!primFlag) {

										String checkForGenerics = refType.toString();

										if (checkForGenerics.contains("<") && checkForGenerics.contains(">")) {
											strGenericsObject = checkForGenerics.substring(
													checkForGenerics.indexOf("<") + 1, checkForGenerics.indexOf(">"));
											submitFieldReferences += "[" + classNames + "]-*[" + strGenericsObject
													+ "],";
										} else {
											submitFieldReferences += "[" + classNames + "]-1[" + refType.toString()
													+ "],";

										}
									}

									/*****
									 * The reference Types can be parsed as
									 * Primitive and NonPrimitive ReferenceTypes
									 *******/

								} else if (refTypeNode instanceof PrimitiveType) {
									PrimitiveType primType = (PrimitiveType) refTypeNode;
									// outputString +=
									// outputString+primType.toString();
									// System.out.println("RefNode" + primType);
									primFlag = true;
									outputReftype = primType.toString();

								}

								else if (refTypeNode instanceof VariableDeclarator) {
									VariableDeclarator varDec = (VariableDeclarator) refTypeNode;
     								outputRefvariable = varDec.toString();

								}

							}

							if (primFlag == true)

							{
								typeString = typeString + submitURLModifier + outputRefvariable + ":" + outputReftype
										+ ";";
								primFlag = false;

							}
							outputString=typeString;
						}
						
						
						
						
						if (bd instanceof MethodDeclaration)
						{
								  
							MethodDeclaration methodDeclaration = (MethodDeclaration) bd;
							
						
							List<Parameter> typeParas= methodDeclaration.getParameters();
		  								
							for(Parameter typePara: typeParas)
							{
							 
								
								methodDecVar = typePara.getType().toString();
								if (interfaceNames.contains(typePara.getType().toString()))
								{
								methodType = "<<interface>>;";
								  }
								else if(ClassListNames.contains(typePara.getType().toString()))
								{
							      	 }
								else{
                  				}
								
							}
							methodDec+=methodDeclaration.getName()+"();";
							
							
	      		    System.out.println("["+classNames+"]uses -.->["+methodType+methodDecVar+"]");
						}

						
						
						
						
						
						
						

					}
					
					//System.out.println("Class"+classNames+" has "+methodDec);

					if (typeString != "") {
						outputString += typeString +"|"+methodDec+"]," + submitFieldReferences;
						System.out.println("dssss"+outputString);

					}	
					
					
     				System.out.println(outputInterface);
					System.out.println(outputImplements);
				
				}

			}
		} catch (Exception e) {
			System.out.println("ErrorMessage:" + e.getMessage());

		}

	}

	/**********
	 * Return the files with .Java Extension to the iterator
	 ******************/

	FileFilter javaFiles = new FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true; // return directories for recursion
			}
			return file.getName().endsWith(".java"); // return .url files
		}
	};

}
