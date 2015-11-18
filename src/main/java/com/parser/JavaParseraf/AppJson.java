
//*********************************************************************************************************?//
package com.parser.JavaParseraf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.portable.InputStream;

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

//*********************************************************************************************************?//

public class AppJson {

	private ArrayList<String> interfaceNames = new ArrayList<String>();
	private ArrayList<String> ClassListNames = new ArrayList<String>();

	JSONObject obj = new JSONObject(); // creation of GLOBAL JSON OBject
										// Containing all structure attributes.

	public String classNames = "";
	public String interfaceName = "";
	static String imageUrl="";
	static String destinationUrl="";
	classType Class_Object[];
	HashMap class_array_list = new HashMap();

	private String[] dataTypes = { "byte", "short", "int", "long", "float", "double", "boolean", "char", "Byte",
			"Short", "Int", "Long", "Float", "Double", "Boolean", "Char", "String" };

	// *********************************************************************************************************?//
	private static String javaFileLocation = "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-2/uml-parser-test-2";

	// private static String javaFileLocation
	// ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-3/uml-parser-test-3";
	// private static String javaFileLocation
	// ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-4/uml-parser-test-4";
	// private static String javaFileLocation =
	// "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1
	// (1)/uml-parser-test-1";
	// **********************************MAIN FUNCTION AND NOTHING MUCH
	// ******************************************************?//
	public static void main(String[] args) {
		AppJson up = new AppJson();
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

	// *********************************************************************************************************?//

	// *************************************CLASS TYPE FUNCTION TO CHECK THE
	// TYPE GETTER SETTER TYPE******************************************?//

	public class classType {
		boolean getter;
		boolean setter;
		boolean private_variable;
		private boolean specialtypeflag;

		classType(boolean getter, boolean setter, boolean private_variable, boolean specialtypeflag) {
			getter = false;
			setter = false;
			private_variable = false;
			specialtypeflag = false;
			if (this.getter == true && this.setter == true) {
				this.specialtypeflag = true;

			}
		}

		boolean getclassType() {

			if (this.getter == true && this.setter == true) {
				this.specialtypeflag = true;

			}

			return this.specialtypeflag;

		}

	}
	// *************************************CLASS TYPE FUNCTION TO CHECK THE
	// TYPE GETTER SETTER TYPE******************************************?//

	// *************************************CLASS TYPE FUNCTION TO CHECK THE
	// TYPE GETTER SETTER TYPE******************************************?//

	public static void saveImage() throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = (InputStream) url.openStream();
		OutputStream os = new FileOutputStream(destinationUrl);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	
	
	public static void popUpImage() {
		try {
			BufferedImage img = ImageIO.read(new File(destinationUrl));
			ImageIcon icon = new ImageIcon(img);
			JLabel label = new JLabel(icon);
			JScrollPane scroller = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JFrame f = new JFrame("UML diagram generated");
			f.getContentPane().add(scroller);
			f.setSize(1000, 3000);
			f.setVisible(true);

			// JOptionPane.showMessageDialog(null,);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public void parseInputFile() {

		/**********
		 * Set the folder from which we need to get the .java files
		 ******************/

		JSONObject global_struct = new JSONObject();

		JSONArray global_array = new JSONArray();

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

				String outputStringI = "[<<interface>>;";
				String outputString = "[";
				String outputInterface = "";
				String outputImplements = "";
				String outputMethodDec = "";

				for (Node cuChildNode : cuChildNodes) {

					if (cuChildNode instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) cuChildNode;

						if (cid.toString().contains("interface")) {
							interfaceNames.add(cid.getName());
							interfaceName = cid.getName();

							JSONObject objinterface = new JSONObject();

							objinterface.put("CIName", cid.getName());
							objinterface.put("CIType", "Interface");

							// objclass//push to Main Array
							global_array.put(objinterface);

						} else {

							classNames = cid.getName();
							// System.out.println(classNames.toString());
							class_array_list.put(classNames.toString(), new classType(false, false, false, false));

							JSONObject objclass = new JSONObject();

							objclass.put("CIName", cid.getName());
							objclass.put("CIType", "Class");

							JSONArray extend = new JSONArray();
							JSONArray implement = new JSONArray();

							if (cid.getExtends() != null) {
								for (Node Extends : cid.getExtends()) {

									outputImplements += "[" + Extends + "^-[" + cid.getName() + "],";

									extend.put(Extends);
								}
								objclass.put("extend", extend);
							}

							if (cid.getImplements() != null) {
								for (Node Implements : cid.getImplements()) {

									outputInterface += "[<<interface>>;" + Implements + "]^-.-[" + cid.getName() + "],";
									implement.put(Implements);
								}
								objclass.put("extends", implement);
							}

							// objclass//push to Main Array
							global_array.put(objclass);

						}
					}
				}

				outputStringI = outputStringI + interfaceName + ";---------------------;";

				outputString = outputString + classNames + "|";
				int interfaceflag;

				global_struct.put("global_struct", global_array);

				for (TypeDeclaration listOfTypeDeclaration : listOfTypeDeclarations) {
					interfaceflag = 0;

					List<BodyDeclaration> listOfBodyDeclarations = listOfTypeDeclaration.getMembers();

					// Determining wheter the accessed is class or a interface.

					if (listOfTypeDeclaration.toString().contains("interface")) {

						// System.out.println("B55D Interface
						// "+listOfBodyDeclarations);
						interfaceflag = 1;

					} else if (listOfTypeDeclaration.toString().contains("class")) {

						// System.out.println("B55D Class
						// "+listOfTypeDeclaration.getName());

					}

					String typeString = "";
					String submitFieldReferences = "";
					String methodType = "";
					String methodDecVar = "";
					String methodDec = "";
					String methodDecI = "";
					JSONArray method = new JSONArray();
					JSONArray field = new JSONArray();
					int JSONpointer;

					for (int i = 0; i < listOfBodyDeclarations.size(); i++) {
						BodyDeclaration bd = listOfBodyDeclarations.get(i);

						String submitURLModifier = "";
						JSONpointer = 0;

						if (bd instanceof FieldDeclaration) {
							// System.out.println("Field Declaration"+bd);

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

						}

						// Determine if a Class is Getter Setter Type Or Normal
						// Type

						if (bd instanceof MethodDeclaration) {

							MethodDeclaration methodDeclaration = (MethodDeclaration) bd;
							List<Parameter> typeParas = methodDeclaration.getParameters();

							// Each Method is printed.// Determine the
							// respective Class of Interface containing the
							// method .
							// System.out.println("@@@@@@Method "+bd);

							method.put(methodDeclaration.getName());

							// iterate over JSON Object

							for (int j = 0; j < global_array.length(); j++) {
								JSONObject object = global_array.getJSONObject(j);
								// System.out.println("Object"+object);
								if (object.get("CIName") == classNames) {
									JSONpointer = j;
									break;
								}

							}

							// traverse and find class and insert the necessary
							// objclass.put("method",method);

							if (methodDeclaration.getName().contains("set")) {

								classType temp_class = (classType) class_array_list.get(classNames.toString());
								temp_class.setter = true;

								if (temp_class.getclassType()) {
									// objclass.getter("setter","true");
									JSONObject object = global_array.getJSONObject(JSONpointer);
									object.put("Setter", "true");

									// System.out.println("\nAtaching Setter at
									// "+JSONpointer);
									global_array.put(JSONpointer, object);

									// System.out.println("GetterSetterClassDetected"+classNames.toString());
									// global_array.put(JSONpointer,"setter=true");

								}

							}

							if (methodDeclaration.getName().contains("get")) {

								classType temp_class = (classType) class_array_list.get(classNames.toString());
								temp_class.getter = true;

								if (temp_class.getclassType()) {

									System.out.println("GetterSetterClassDetected" + classNames.toString());
									JSONObject objectgetter = global_array.getJSONObject(JSONpointer);
									// System.out.println("\nAtaching Getter at
									// "+JSONpointer);
									objectgetter.put("Getter", "true");

									global_array.put(JSONpointer, objectgetter);

								}

							}

							boolean primFlag = false;
							for (Parameter typePara : typeParas) {

								primFlag = false;

								methodDecVar = typePara.getType().toString();
								// Detect if the Method Type if of primitive
								// Type

								for (String str : dataTypes) {
									if (methodDecVar.toString().contains(str)) {

										// setup the primitive Flag

										primFlag = true;
										methodDecVar = ""; // jugaad reset
															// MethodDec if Prim
															// Flag is true
									}

								}

								if (interfaceNames.contains(typePara.getType().toString())) {
									methodType = "<<interface>>;";
								} else if (ClassListNames.contains(typePara.getType().toString())) {
								} else {
								}

							}

							classType temp_class = (classType) class_array_list.get(classNames.toString());

							if (interfaceflag == 1)
								methodDecI += methodDeclaration.getName() + "();";

							else if (!temp_class.getclassType()) {
								methodDec += methodDeclaration.getName() + "();";

							}

							// System.out.println(primFlag);
							if (methodDecVar != "" && (!primFlag)) {

								System.out.println("[" + classNames + "]uses -.->[" + methodType + methodDecVar + "]");
							}

						}
						JSONObject object = global_array.getJSONObject(JSONpointer);

						object.put("method", method);
						global_array.put(JSONpointer, object);
					}

					if (methodDecI != "") {
						outputStringI += ";---------------------;" + methodDecI + "]";
						System.out.println(outputStringI);
					}

					if (typeString != "") {
						outputString += typeString + "|" + methodDec + "]," + submitFieldReferences;
						outputStringI += "|" + methodDecI + "]";
						System.out.println(outputString);

					}

					System.out.println(outputInterface);
					System.out.println(outputImplements);

				}

			}

			try {
				global_struct.put("global_struct", global_array);
				System.out.print(global_struct);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
