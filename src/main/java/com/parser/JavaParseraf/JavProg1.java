package com.parser.JavaParseraf;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;

public class JavProg1 {
	
	private static String javaFileLocation = "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1 (1)/uml-parser-test-1";
	private String umlURL = "http://yuml.me/diagram/scruffy/class/";

	private String[] dataTypes = { "byte", "short", "int", "long", "float", "double", "boolean", "char", "Byte",
			"Short", "Int", "Long", "Float", "Double", "Boolean", "Char" };
	private String classNames = "";
	private ArrayList<String> interfaceNames = new ArrayList<String>();

	
	public JavProg1() {
	}

	
	
	public static void main(String[] args) {
		JavProg1 up = new JavProg1();
		up.parseInput();
	}
	
	
	

	public void parseInput() {
		try {
			// Set the folder from which we need to get the .java files
			File location = new File(javaFileLocation);

			// Get the .java files from the folder using FileFilter
			for (File javaFile : location.listFiles(javaFiles)) {
				// Get the stream for the file
				FileInputStream inputStream = new FileInputStream(javaFile.getAbsolutePath());

				// Parses the file
				CompilationUnit cu = JavaParser.parse(inputStream);

				System.out.println("cu--"+cu+"--cu");


				
				// Get the Child nodes of the File. It contains one of the
				// following files
				// 1. ClassOrInterfaceDeclaration
				// 2. PackageDeclaration
				// 3. ImportDeclaration
				List<Node> cuChildNodes = cu.getChildrenNodes();

				// Check for each node
				for (Node cuChildNode : cuChildNodes) {
					if (cuChildNode instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) cuChildNode;

						// Check whether the returned class declaration is an
						// interface
						if (cid.toString().contains("interface")) {
							interfaceNames.add(cid.getName());
							System.out.println(interfaceNames);
						} else {

							System.out.println(cid.getName().toString() + " class name");
							classNames = cid.getName();
						}

					} else if (cuChildNode instanceof PackageDeclaration) {
						// PackageDeclaration pd =
						// (PackageDeclaration)cuChildNode;
						// no action needed here currently
					} else if (cuChildNode instanceof ImportDeclaration) {
						// ImportDeclaration ide =
						// (ImportDeclaration)cuChildNode;
						// no action needed here currently
					}
				}
				// Get the TypeDeclarations for the Body of the Java File
				// 1. FieldDeclaration
				// 2. MethodDeclaration
				List<TypeDeclaration> listOfTypeDeclarations = cu.getTypes();

				for (TypeDeclaration id : listOfTypeDeclarations) {

					//
					List<BodyDeclaration> listOfBodyDeclarations = id.getMembers();

					for (int j = 0; j < listOfBodyDeclarations.size(); j++) {

						BodyDeclaration bd = listOfBodyDeclarations.get(j);

						if (bd instanceof FieldDeclaration) {
							FieldDeclaration fd = (FieldDeclaration) bd;

							int modifiers = fd.getModifiers();
							String submitURLVariable = "", submitURLModifier = "";
							boolean primitiveMatch = false;

							switch (modifiers) {
							case ModifierSet.PRIVATE:
								System.out.println("Variable is private");
								submitURLModifier = "-";
								break;
							case ModifierSet.PUBLIC:
								System.out.println("Variable is public");
								submitURLModifier = "+";
								break;
							case ModifierSet.PROTECTED:
								System.out.println("Variable is protected");
								submitURLModifier = "~";
								break;
							}
							List<Node> listNodes = fd.getChildrenNodes();

							for (Node n : listNodes) {
								if (n instanceof ReferenceType) {
									ReferenceType rt = (ReferenceType) n;
									System.out.println(rt.getType().toString() + " reference type");
									Type rType = rt.getType();

									for (String str : dataTypes) {
										if (rType.toString().contains(str)) {
											submitURLVariable += rType.toString() + "(*)";
											System.out.println(submitURLVariable + " submitURLVariable");
											primitiveMatch = true;
											break;
										}
									}

									if (!primitiveMatch) {
										String checkForGenerics = rt.getType().toString();
										String strGenericsObject = "";
										String submitFieldReferences = null;
										if (checkForGenerics.contains("<") && checkForGenerics.contains(">")) {
											strGenericsObject = checkForGenerics.substring(
													checkForGenerics.indexOf("<") + 1, checkForGenerics.indexOf(">"));
											submitFieldReferences += "[" + classNames + "]-*[" + strGenericsObject
													+ "],";
										} else {
											submitFieldReferences += "[" + classNames + "]-1[" + rt.getType().toString()
													+ "],";
										}
									}
								} else if (n instanceof PrimitiveType) {
									PrimitiveType pt = (PrimitiveType) n;
									System.out.println(pt.toString() + " primitive type");
									primitiveMatch = true;
									submitURLVariable += pt.toString();
								} else if (n instanceof VariableDeclarator) {
									VariableDeclarator vd = (VariableDeclarator) n;
									System.out.println(vd.toString() + " variable declartor");
								}
							}
						} else if (bd instanceof MethodDeclaration) {
							MethodDeclaration md = (MethodDeclaration) bd;
							System.out.println(
									md.getName() + " method name" + md.getParameters() + " are the parameters");

							List<Parameter> paramters = md.getParameters();

							for (Parameter p : paramters) {
								List<Node> childNodes = p.getChildrenNodes();
								for (Node n : childNodes) {
									boolean primitiveMatchInMethod = false;
									if (n instanceof ReferenceType) {
										ReferenceType rt = (ReferenceType) n;
										System.out.println(rt.getType().toString() + " reference type");
										Type rType = rt.getType();

										for (String str : dataTypes) {
											if (rType.toString().contains(str)) {
												primitiveMatchInMethod = true;
											}
										}

										boolean interfaceFoundHere = false;
										if (!primitiveMatchInMethod) {
											for (String s : interfaceNames) {
												if (s.equals(rt.getType().toString())) {
													interfaceFoundHere = true;
													break;
												}
											}

											String submitMethodReferences = null;
											if (interfaceFoundHere) {
												submitMethodReferences += "[" + classNames + "]uses-.->[<<interface>>;"
														+ rt.getType().toString() + "],";
											} else {
												submitMethodReferences += "[" + classNames + "]uses-.-["
														+ rt.getType().toString() + "],";
											}
										}
									}
								}
							}
						}
					}

					ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) id;
					List<ClassOrInterfaceType> getExtend = cid.getExtends();
					List<ClassOrInterfaceType> getImplement = cid.getImplements();

					if (getExtend != null) {
						if (getExtend.size() > 0) {
							for (ClassOrInterfaceType extendThis : getExtend) {

							}
						}
					}

					if (getImplement != null) {
						if (getImplement.size() > 0) {
							for (ClassOrInterfaceType interfaceThis : getImplement) {

							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	FileFilter javaFiles = new FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true; // return directories for recursion
			}
			return file.getName().endsWith(".java"); // return .url files
		}
	};
}