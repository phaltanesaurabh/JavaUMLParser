
//*********************************************************************************************************?//

package com.parser.JavaParseraf;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.parser.JavaParseraf.AppJson.classType;

//*********************************************************************************************************?//

public class AppStruct {

	// *********************************************************************************************************?//
	//private static String javaFileLocation="C:/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-2/uml-parser-test-2";
	private static String javaFileLocation = null;
	// 
	//

	// 0

	// private static String javaFileLocation =
	// "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1
	// (1)/uml-parser-test-1";

	// ;
	//
	// (1)/uml-parser-test-1";
	//
	// (1)/uml-parser-test-1";
	// private static String javaFileLocation
	// ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-5/uml-parser-test-5";
	//
	//
	//

	// private static String javaFileLocation
	// ="/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-3/uml-parser-test-3";
	//
	// //
	//
	//
	// private static String javaFileLocation =
	// "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-4/uml-parser-test-4";
	//

	//private static String javaFileLocation = null;
	//
	//
	// "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-4/uml-parser-test-4";
	// private static String javaFileLocation =
	// "/Users/saurabh-pc/Desktop/PaulJava/sample";
	// Desktop\PaulJava\sampletest
	//
	// private static String javaFileLocation =
	// "/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-1
	// (1)/uml-parser-test-1";
	// **********************************MAIN FUNCTION AND NOTHING MUCH
	// ******************************************************?//

	static JSONObject global_struct = new JSONObject();
	//public static String destinationURL = "image.png";
	public static String destinationURL = null;
	public static String imageUrl = null;
	JSONArray global_array = new JSONArray();
	private String[] dataTypes = { "byte", "short", "int", "long", "float", "double", "boolean", "char", "Byte",
			"Short", "Int", "Long", "Float", "Double", "Boolean", "Char", "String" };
	public static String YumlutputString = "";

	public static void main(String[] args) {

		javaFileLocation = args[0];
		System.out.println("Cmd 0 -"+javaFileLocation);
		destinationURL = args[1];
		System.out.println("Cmd 1 -"+destinationURL);
		AppStruct up = new AppStruct();
		up.parseInputFile(); // Pass1
		up.structureBuilder(); // Pass2
		System.out.println("\n\n\n\n\n\n\n\n" + global_struct);//
		up.relationShipBuilder(); // RelationShip Builder

		System.out.println(YumlutputString);
		imageUrl = new String("http://yuml.me/diagram/class/%2F%2F Cool Class Diagram, ");
		
		try {
			YumlutputString = URLEncoder.encode(YumlutputString, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		imageUrl = imageUrl + YumlutputString;
		
		System.out.println(imageUrl);
		try {
			saveImage();
			popUpImage();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void popUpImage() {
		try {
			BufferedImage img = ImageIO.read(new File(destinationURL));
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

	/*
	 * public static void saveImage() throws IOException { URL url = new
	 * URL(imageUrl); InputStream is = (InputStream) url.openStream();
	 * OutputStream os = new FileOutputStream(destinationURL);
	 * 
	 * byte[] b = new byte[2048]; int length;
	 * 
	 * while ((length = is.read(b)) != -1) { os.write(b, 0, length); }
	 * 
	 * is.close(); os.close(); }
	 */

	// lets say saving the png file from google.

	public static void saveImage() throws IOException {
		
		
		URL url = new URL(imageUrl);
		URLConnection urlConnection = url.openConnection();

		// creating the input stream from google image
		BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
		// my local file writer, output stream
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destinationURL));

		// until the end of data, keep saving into file.
		int i;
		while ((i = in.read()) != -1) {
			out.write(i);
		}
		out.flush();

		// closing all the shits
		out.close();
		in.close();

	}

	public void relationShipBuilder() {

		JSONObject jsonobj = global_struct;

		JSONArray jsonobj_array = new JSONArray();
		JSONArray assCheckerArray = new JSONArray();
		try {
			jsonobj_array = jsonobj.getJSONArray("global_struct");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < jsonobj_array.length(); i++) {

			try {
				JSONObject jsonCIName = jsonobj_array.getJSONObject(i);

				// get extends and Get implements and replace the necessary with
				// the self structs of the same.
				// System.out.println(jsonCIName.get("selfStruct"));
				JSONArray implement = (JSONArray) jsonCIName.get("implements");
				JSONArray extend = (JSONArray) jsonCIName.get("extend");
				JSONArray usesStruct = (JSONArray) jsonCIName.get("usesStruct");
				JSONArray association = (JSONArray) jsonCIName.get("Association");

				if (implement.length() != 0) {

					for (int k = 0; k < implement.length(); k++) {
						// [<<interface>>;A1]^-.-[B1],

						int value = 0;
						String search_string = implement.get(k).toString();
						for (int m = 0; m < jsonobj_array.length(); m++) {
							if (jsonobj_array.getJSONObject(m).get("CIName").equals(search_string)) {
								value = m;

							}

						}

						// System.out.println(jsonobj_array.getJSONObject(value).get("selfStruct")
						// + "^-.-"
						// + jsonCIName.get("selfStruct").toString());

						YumlutputString += jsonobj_array.getJSONObject(value).get("selfStruct") + "^-.-"
								+ jsonCIName.get("selfStruct").toString() + ",";
					}

				}

				if (extend.length() != 0) {

					for (int k = 0; k < extend.length(); k++) {
						// [<<interface>>;A1]^-.-[B1],

						int value = 0;
						String search_string = extend.get(k).toString();
						for (int m = 0; m < jsonobj_array.length(); m++) {
							if (jsonobj_array.getJSONObject(m).get("CIName").equals(search_string)) {
								value = m;

							}

						}

						// System.out.println(jsonobj_array.getJSONObject(value).get("selfStruct")
						// + "^-"
						// + jsonCIName.get("selfStruct").toString());

						YumlutputString += jsonobj_array.getJSONObject(value).get("selfStruct") + "^-"
								+ jsonCIName.get("selfStruct").toString() + ",";
					}

				}

				if (usesStruct.length() != 0) {

					for (int h = 0; h < usesStruct.length(); h++) {
						JSONObject usesStructObject = new JSONObject();
						usesStructObject = usesStruct.getJSONObject(h);
						// System.out.println("UsesStruct"+usesStruct);

						// System.out.println("\nUses :" + usesStructObject);

						for (int k = 0; k < usesStruct.length(); k++) {
							// [<<interface>>;A1]^-.-[B1],

							int value = 0;

							if (usesStructObject.get("usesTarget").toString() != null) {

								String searchsource = usesStructObject.get("usesTarget").toString();
								for (int m = 0; m < jsonobj_array.length(); m++) {
									if (jsonobj_array.getJSONObject(m).get("CIName").equals(searchsource)) {
										value = m;

									}

								}

							}
							if (!jsonCIName.get("CIName").toString()
									.equals(jsonobj_array.getJSONObject(value).get("CIName").toString())) {
								// System.out.println(jsonCIName.get("selfStruct")
								// + "uses -.->"
								// +
								// jsonobj_array.getJSONObject(value).get("selfStruct"));
								if (jsonobj_array.getJSONObject(value).get("CIType") != "Class") {
									YumlutputString += jsonCIName.get("selfStruct") + "uses-.->"
											+ jsonobj_array.getJSONObject(value).get("selfStruct") + ",";

								}

							}

						}

					}

				}
				//
				if (association.length() != 0) {

					/*
					 * System.out.println(jsonCIName.get("CIName"));
					 * System.out.println("association1" + association.length()
					 * + association);
					 */

					for (int h = 0; h < association.length(); h++) {

						JSONObject associationObject = association.getJSONObject(h);

						String checker1 = jsonCIName.get("CIName").toString()
								+ associationObject.get("associatedTo").toString();

						String checker2 = associationObject.get("associatedTo").toString()
								+ jsonCIName.get("CIName").toString();

						if (assCheckerArray.length() == 0 || (!assCheckerArray.toString().contains(checker1))
								|| (!assCheckerArray.toString().contains(checker2))) {

							String search_string = associationObject.get("associatedTo").toString();

							int value = 0;
							for (int m = 0; m < jsonobj_array.length(); m++) {
								if (jsonobj_array.getJSONObject(m).get("CIName").equals(search_string)) {
									value = m;

								}

							}

							// System.out.println("Here"+jsonobj_array.getJSONObject(value).get("CIName"));

							// JSONObject assocObject=new JSONObject();
							// assocObject=jsonobj_array.getJSONObject(value);

							// System.out.println(associationObject.get("associatedTo"));

							// System.out.println(
							// jsonCIName.get("selfStruct") + "-" +
							// associationObject.get("associatedMult") + "["
							// +
							// jsonobj_array.getJSONObject(value).get("selfStruct")
							// + "],");

							YumlutputString += jsonCIName.get("selfStruct") + "-"
									+ associationObject.get("associatedMult") + "["
									+ jsonobj_array.getJSONObject(value).get("selfStruct") + "]," + ",";

							assCheckerArray.put(checker1);
							assCheckerArray.put(checker2);

						}

					}

					// association.get("associatedTo").toString();

					// Check the Association Checker if not present print.
					// Stringn stringPointer=+

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private String arrayImplement(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	public void structureBuilder() {

		// System.out.println(global_struct);
		// traverse and generate the output
		// JSONObject jsonobj = global_struct;

		JSONArray jsonobj_array = new JSONArray();

		try {
			jsonobj_array = global_struct.getJSONArray("global_struct");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for (int it=0 ; it < items.length() ; it++){ JSONObject contactItem =
		 * items.getJSONObject(it); String userName =
		 * contactItem.getString("name");
		 * System.out.println("Name----------"+userName); }
		 */

		// System.out.println(jsonobj_array);

		for (int i = 0; i < jsonobj_array.length(); i++) {

			try {
				JSONObject jsonCIName = jsonobj_array.getJSONObject(i);
				// if (jsonCIName.get("CIType").equals("Class")) {

				// [A|-x:int;-y:int(*);|],[A]-*[B],[A]-1[C],[A]-*[D],
				// ClassName:
				String className = (String) jsonCIName.get("CIName");
				if (jsonCIName.get("CIType").equals("Class")) {
					className = (String) jsonCIName.get("CIName");
				}
				if (jsonCIName.get("CIType").equals("Interface")) {
					className = "＜＜interface＞＞;" + (String) jsonCIName.get("CIName");
				}
				// System.out.println(className); //All classess Traversed
				// Fields:
				JSONArray CIfields = new JSONArray();
				JSONArray CImethods = new JSONArray();

				// System.out.println(CIfields);
				String classvariables = "";
				String varaibleStringOutput = "";
				String genOutputString = "";

				try {

					// System.out.println(jsonCIName.get("CIName"));
					CIfields = (JSONArray) jsonCIName.get("Fields");
					JSONArray associationArray = new JSONArray();
					if (CIfields.length() != 0)
						for (int j = 0; j < CIfields.length(); j++) {

							String varaibleString = "";
							// System.out.println(CIfields);
							try {
								JSONObject jsonCIfield = CIfields.getJSONObject(j);

								JSONObject relationAssociation = new JSONObject();

								// System.out.println(jsonCIfield);
								// System.out.println(jsonCIfield.get("fieldType"));
								// Logic Begins to determine the Type of Field
								// Type
								String fieldType = (String) jsonCIfield.get("fieldType");

								// System.out.println(jsonCIfield.get("fieldmodifier").toString());

								/*
								 * if(
								 * jsonCIfield.get("fieldmodifier").toString().
								 * contains("#")) continue;
								 */

								String switchcase = jsonCIfield.get("fieldTypePrim").toString();

								if (switchcase.contains("1")) {
									varaibleString = jsonCIfield.get("fieldmodifier").toString()
											+ jsonCIfield.get("fieldvaraiable").toString() + ":"
											+ jsonCIfield.get("fieldType").toString();

								} else if (switchcase.contains("2")) {

									String genericType = jsonCIfield.get("fieldType").toString();

									// System.out.println(genericType);
									// System.out.println("\nsaurah"+jsonCIfield);

									String genOutput = "";

									if (genericType.contains("<") && genericType.contains(">")) {

										genOutput = genericType.substring(genericType.indexOf("<") + 1,
												genericType.indexOf(">"));

										// System.out.println("genOutput
										// "+genericType+genOutput);

										if (genOutput != "") {
											relationAssociation.put("associatedTo", genOutput);
											relationAssociation.put("associatedMult", "*");
											associationArray.put(relationAssociation);
										}

										genOutputString += "[" + jsonCIName.get("CIName") + "]-*[" + genOutput + "],";
									} else {

										genOutputString += "[" + jsonCIName.get("CIName") + "]-1[" + genericType + "],";

										if (genericType != "") {
											relationAssociation.put("associatedTo", genericType);
											relationAssociation.put("associatedMult", "1");
											associationArray.put(relationAssociation);

										}

									}

								} else if (switchcase.contains("3")) {

									if (!jsonCIfield.get("fieldmodifier").toString().equals("#")) {
										varaibleString = jsonCIfield.get("fieldmodifier").toString()
												+ jsonCIfield.get("fieldvaraiable").toString() + ":"
												+ jsonCIfield.get("fieldType").toString();

									}

									// System.out.println("genOutput
									// "+jsonCIfield);

								}

								if (!varaibleString.isEmpty()) {

									varaibleStringOutput += varaibleString + ";";
								}

								jsonCIName.put("Association", associationArray);

							} catch (Exception e) {

								j++;

							}

							// fieldType primitive or nonPrimitive;

							// fieldmodifier+fieldvaraiable:fieldType

						}

				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println("teeee");
				}

				String outputMethodString = "";

				try {
					//
					CImethods = (JSONArray) jsonCIName.get("Methods");

					// lol

					// System.out.println("Here we go "+CImethods.length());

					boolean getter = false;
					boolean setter = false;

					String getterreplace = "";
					String setterreplace = "";

					if (CImethods.length() != 0)
						for (int j = 0; j < CImethods.length(); j++) {

							JSONObject jsonCImethod = CImethods.getJSONObject(j);

							String methodType = (String) jsonCImethod.get("methodType");
							// System.out.println("Now atvbvcb
							// "+jsonCImethod.get("methodmodifier").toString());
							if (jsonCImethod.get("methodmodifier").toString().contains("-")) {

								continue;
							}
							String methodmodifier = (String) jsonCImethod.get("methodmodifier");
							String methodName = (String) jsonCImethod.get("methodName");
							JSONArray methodParas1 = (JSONArray) jsonCImethod.get("methodParameter");

							// jsonCImethod.get("methodParameter");
							String methodParOutput = "";
							String methodParvar = "";
							String methodParaName = "";
							JSONArray methodparaOutputArray = new JSONArray();

							if (methodParas1.length() != 0) {
								for (int m = 0; m < methodParas1.length(); m++) {
									System.out.println(methodParas1.getJSONObject(m).get("mName"));
									methodParaName = (String) methodParas1.getJSONObject(m).get("mName");
									methodParvar = (String) methodParas1.getJSONObject(m).get("mVar");

									// methodParOutput+=methodParaName;

									if ((!methodParvar.isEmpty()) || (!methodParaName.isEmpty())) {
										methodParOutput = methodParvar + ":" + methodParaName;
										// System.out.println(methodParOutput1);
										System.out.println("test");
									}

								}

							}

							// Determine the Getter Setter Function :
							if (jsonCImethod.get("methodName").toString().contains("get")) {
								getter = true;
								getterreplace = methodmodifier + methodName + "(" + methodParOutput + ")" + ":"
										+ methodType + ";";
							}

							if (jsonCImethod.get("methodName").toString().contains("set")) {

								// jsonCImethod.get("methodName").toString()
								setterreplace = methodmodifier + methodName + "(" + methodParOutput + ")" + ":"
										+ methodType + ";";

								setter = true;
							}

							int flag = 0;
							if (getter && setter) {
								// System.out.println()
								// System.out.println("Getter Setter Function
								// Detected" + jsonCImethod);

								String getsetmethod = jsonCImethod.get("methodName").toString().substring(3);

								// saurabh

								// Check this Method is respective Class
								// Structure
								// GetClass of the Given Function

								JSONArray getterSetter = jsonCIName.getJSONArray("Fields");
								int value = 0;

								for (int h = 0; h < getterSetter.length(); h++) {
									JSONObject gettersetterObj = (JSONObject) getterSetter.get(h);
									// System.out.println(gettersetterObj.get("fieldvaraiable").toString()+);
									if (gettersetterObj.get("fieldvaraiable").toString()
											.equals(getsetmethod.toLowerCase())) {
										value = h;
										if (gettersetterObj.get("fieldmodifier").toString().contains("-")) {
											gettersetterObj.put("fieldmodifier", "+");

											// System.out.println("Now at
											// :"+gettersetterObj);
											jsonCIName.getJSONArray("Fields").put(h, gettersetterObj);
											/// System.out.println(jsonCIName);

											// jsonobj_array.put(i, jsonCIName);
											// System.out.println("this"+jsonobj_array.get(i));

											JSONObject insertGetterSetter = new JSONObject();
											// insertGetterSetter=jsonobj_array.get();
											insertGetterSetter = (JSONObject) jsonobj_array.get(i);

											// System.out.println(jsonobj_array);
											jsonobj_array.put(h, insertGetterSetter);

											global_struct.put("global_struct", jsonobj_array);

											flag = 1;

											varaibleStringOutput = varaibleStringOutput.replaceAll(
													"-" + getsetmethod.toLowerCase(), "+" + getsetmethod.toLowerCase());
													// System.out.println("varaibleStringOutput"+result);

											// System.out.println("look
											// tjkdsjfkdsis:"+global_struct);
											// System.out.println(" This is CI
											// Object "+i);

										}
									}
								}

								getter = false;
								setter = false;
							}
							// System.out.println("@@@@@@@@Printting this for
							// Gett Setter@@@@@@@@@");
							// System.out.println(CImethods);

							outputMethodString += methodmodifier + methodName + "(" + methodParOutput + ")" + ":"
									+ methodType + ";";

							if (flag == 1) {
								outputMethodString = outputMethodString.replace(getterreplace, "");
								outputMethodString = outputMethodString.replace(setterreplace, "");

							}

							if (!jsonCImethod.get("methodParameter").toString().isEmpty()) {

								// System.out.println("Echoing Method Parameters
								// for Uses Logic");
								// System.out.println("Names " +
								// jsonCIName.get("CIName"));
								/// System.out.println("MethodP " +
								// jsonCImethod.get("methodParameter"));

								JSONArray methodParas = (JSONArray) jsonCImethod.get("methodParameter");

								// jsonCImethod.get("methodParameter");

								JSONArray usesStructArray = new JSONArray();
								for (int l = 0; l < methodParas.length(); l++) {

									JSONObject mObject = new JSONObject();

									// System.out.println(methodParas.getJSONObject(l));
									mObject = methodParas.getJSONObject(l);

									String output = "";
									// saur
									JSONObject usesStruct = new JSONObject();

									if (mObject.get("mType").toString().contains("genericType")) { // Primitive
																									// //PrimitiveArray
																									// System.out.println(mObject.get("mName"));

										String usesSource = jsonCIName.get("CIName").toString();
										String usesTarget = mObject.get("mName").toString();

										output = jsonCIName.get("CIName") + "uses" + mObject.get("mName");
										usesStruct.put("usesSource", usesSource);
										usesStruct.put("usesTarget", usesTarget);
									} else {

										usesStruct.put("usesSource", "");
										usesStruct.put("usesTarget", "");

									}

									usesStructArray.put(usesStruct);
									jsonCIName.put("usesStruct", usesStructArray);
									// System.out.println("usesStructArray"+usesStructArray);

									// System.out.println("UsesStruct"+usesStructArray);
								}

								String output = "";

								// Determine the Type of Method Parameters and
								// if of NonPrimitive Type found in Classes or
								// Interfaces
								// Those Uses Attributes

							}

							// System.out.println();

						}

				} catch (Exception e) {
					e.printStackTrace();
				}

				// Methods

				String output = "[";
				output += className + "|" + varaibleStringOutput + "|" + outputMethodString;
				output += "]";

				// System.out.println("loreeeeees:"+global_struct);

				// Push this to internal Object.selfStruct
				jsonCIName.put("selfStruct", output);

				// PrintIndividual Class Attributes

				// System.out.println(output);
				// System.out.println(genOutputString);

				// }

				/*
				 * else if (jsonCIName.get("CIType").equals("Interface")) {
				 * //System.out.println("INterface" + jsonCIName);
				 * 
				 * }
				 */

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void parseInputFile()

	{

		File location = new File(javaFileLocation);
		try {

			for (File javaFile : location.listFiles(javaFiles)) {

				FileInputStream inputStream = new FileInputStream(javaFile.getAbsolutePath());
				CompilationUnit cu = JavaParser.parse(inputStream);
				List<Node> cuChildNodes = cu.getChildrenNodes();
				List<TypeDeclaration> listOfTypeDeclarations = cu.getTypes();

				JSONObject objintclass = new JSONObject();

				for (Node cuChildNode : cuChildNodes) {

					if (cuChildNode instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) cuChildNode;

						JSONArray method = new JSONArray();
						JSONArray extend = new JSONArray();
						JSONArray field = new JSONArray();
						JSONArray selfstruct = new JSONArray();
						JSONArray implement = new JSONArray();
						JSONArray usesstruct = new JSONArray();
						JSONArray usesStruct = new JSONArray();
						JSONArray association = new JSONArray();

						if (cid.toString().contains("interface")) {

							objintclass.put("CIName", cid.getName());
							objintclass.put("CIType", "Interface");
							objintclass.put("Fields", field);
							objintclass.put("Methods", method);
							objintclass.put("extend", extend);
							objintclass.put("implements", implement);
							objintclass.put("selfstruct", selfstruct);
							objintclass.put("usesStruct", usesstruct);
							objintclass.put("Association", association);

						} else {

							objintclass.put("CIName", cid.getName());
							objintclass.put("CIType", "Class");
							objintclass.put("Fields", field);
							objintclass.put("Methods", method);
							objintclass.put("extend", extend);
							objintclass.put("implements", implement);
							objintclass.put("selfstruct", selfstruct);
							objintclass.put("usesStruct", usesstruct);
							objintclass.put("Association", association);

							if (cid.getExtends() != null) {
								for (Node Extends : cid.getExtends()) {

									extend.put(Extends);
								}

								objintclass.put("extend", extend);

							}

							if (cid.getImplements() != null) {
								for (Node Implements : cid.getImplements()) {
									implement.put(Implements);

								}
								objintclass.put("implements", implement);

							}

							// objclass//push to Main Array

						}
					}
				}

				global_array.put(objintclass);

				for (TypeDeclaration listOfTypeDeclaration : listOfTypeDeclarations) {

					List<BodyDeclaration> listOfBodyDeclarations = listOfTypeDeclaration.getMembers();

					boolean flag = false;
					String classorinterface = listOfTypeDeclaration.getName();
					int JSONpointer;

					JSONArray fieldarray = new JSONArray();

					JSONArray methodarray = new JSONArray();
					for (int i = 0; i < listOfBodyDeclarations.size(); i++) {
						BodyDeclaration bd = listOfBodyDeclarations.get(i);

						JSONObject fieldmethod = new JSONObject();
						JSONObject field = new JSONObject();
						JSONObject method = new JSONObject();
						JSONpointer = 0;

						if (bd instanceof FieldDeclaration) {
							// types can be primitive/nonPrimitive
							// System.out.println(bd);
							flag = true;
							FieldDeclaration fd = (FieldDeclaration) bd;
							int modifiers = fd.getModifiers();
							List<Node> listNodes = fd.getChildrenNodes();
							String fieldType = fd.getType().toString();
							String fieldvaraiable = "";

							String submitURLModifier = "#";

							switch (modifiers) {
							case 2:
								submitURLModifier = "-";
								break;
							case 3:
								submitURLModifier = "+";
								break;
							case 4:
								submitURLModifier = "#";
								break;
							}
							int fieldTypePrim = 0;
							for (Node refTypeNode : listNodes) {

								if (refTypeNode instanceof ReferenceType) {
									ReferenceType refType = (ReferenceType) refTypeNode;
									// System.out.println("Ref +: "+refType);
									fieldTypePrim = 2;

									for (String str : dataTypes) {
										if (refType.toString().contains(str)) {
											fieldTypePrim = 3;
											fieldType = refType.getType().toString() + "(*)";
											if (refType.getType().toString().equals("String")) {
												fieldType = "String";
											}
										}

									}

								}

								else if (refTypeNode instanceof PrimitiveType) {
									fieldTypePrim = 1;

								}

								else if (refTypeNode instanceof VariableDeclarator) {
									VariableDeclarator varDec = (VariableDeclarator) refTypeNode;
									fieldvaraiable = varDec.toString();

								}

							}

							field.put("fieldType", fieldType);

							// System.out.println(fd.getType().toString());

							field.put("fieldTypePrim", fieldTypePrim);

							field.put("fieldmodifier", submitURLModifier);
							field.put("fieldvaraiable", fieldvaraiable);
							fieldarray.put(field);
							// System.out.println("Fieldarray:"+fieldarray);

						}

						else if (bd instanceof MethodDeclaration) {

							flag = false;

							String methodName = ((MethodDeclaration) bd).getName().toString();
							String methodType = ((MethodDeclaration) bd).getType().toString();

							// System.out.println(((MethodDeclaration)
							// bd).getType().toString());

							MethodDeclaration methodDeclaration = (MethodDeclaration) bd;
							List<Parameter> typeParas = methodDeclaration.getParameters();
							List<TypeParameter> typeParas1 = methodDeclaration.getTypeParameters();

							// System.out.println("Here"+methodDeclaration.getParameters());

							JSONArray methodparameters = new JSONArray();
							JSONArray methodpara = new JSONArray();
							JSONArray methodParam = new JSONArray();
							for (Parameter typePara : typeParas) {

								JSONObject methodparameterObject = new JSONObject();
								String mType = "Primitive";

								String mName = typePara.getType().toString();

								String[] mVarArray = typePara.toString().split("\\s+");
								String mVar = mVarArray[1];
								System.out.println("here" + mVar);

								boolean prim = false;

								// System.out.println(mName);

								for (String str : dataTypes) {
									if (mName.toString().contains(str)) {

										mType = "PrimitiveArray";
										prim = true;
									}

								}
								if (!prim) {

									mType = "genericType";
								}

								mName = mName.replace("<", "＜").replace(">", "＞").replace("[", "［").replace("]", "］");

								methodparameterObject.put("mName", mName);
								methodparameterObject.put("mType", mType);
								methodparameterObject.put("mVar", mVar);
								methodParam.put(methodparameterObject);

							}
							// methodparameters.put(methodParam);

							// Determine the Type of Parameters

							int modifiers = ((MethodDeclaration) bd).getModifiers();
							// System.out.println(modifiers);
							String methodModifier = "";

							switch (modifiers) {
							case 1:
								methodModifier = "+";
								break;
							case 2:
								methodModifier = "-";
								break;
							case 4:
								methodModifier = "#";
								break;
							}

							method.put("methodType", methodType);
							method.put("methodmodifier", methodModifier);
							method.put("methodName", methodName);
							method.put("methodParameter", methodParam);
							methodarray.put(method);

							// System.out.println("Methodparat" + method);

							// parameter has parameterName and parameter Type

						} else if (bd instanceof ConstructorDeclaration) {

							ConstructorDeclaration constructorName = (ConstructorDeclaration) bd;

							List<Parameter> consParams = constructorName.getParameters();

							String constNam = constructorName.getName().toString();

							JSONArray constParaTypeArray = new JSONArray();
							// String methodType = ((ConstructorDeclaration)
							// bd).getType().toString();

							for (Parameter typePara : consParams) {
								JSONObject constParameter = new JSONObject();

								String mName = typePara.getType().toString();

								String[] mVarArray = typePara.toString().split("\\s+");
								String mVar = mVarArray[1];
								System.out.println("here" + mVar);

								// \\ String mType=//to be done

								String mType = "Primitive";

								boolean prim = false;
								for (String str : dataTypes) {
									if (mName.toString().contains(str)) {

										mType = "PrimitiveArray";
										prim = true;

									}

								}
								if (!prim) {

									mType = "genericType";
								}

								mName = mName.replace("<", "＜").replace(">", "＞").replace("[", "［").replace("]", "］");

								// System.out.print("reoa"+mName);
								constParameter.put("mName", mName);
								constParameter.put("mType", mType);
								constParameter.put("mVar", mVar);

								constParaTypeArray.put(constParameter);
							}

							int modifiers = ((ConstructorDeclaration) bd).getModifiers();
							// System.out.println(modifiers);
							String methodModifier = "";

							switch (modifiers) {
							case 1:
								methodModifier = "+";
								break;
							case 2:
								methodModifier = "-";
								break;
							case 4:
								methodModifier = "#";
								break;
							}

							method.put("methodType", "");
							method.put("methodmodifier", methodModifier);
							method.put("methodName", constNam);
							method.put("methodParameter", constParaTypeArray);
							methodarray.put(method);

							// System.out.println("Constructor "+method);

						}

						// System.out.println(fieldarray);
						JSONpointer = 0;
						for (int j = 0; j < global_array.length(); j++) {
							JSONObject object = global_array.getJSONObject(j);
							// System.out.println("Object"+object);
							if (object.get("CIName") == classorinterface) {
								JSONpointer = j;
								break;
							}

						}

						JSONObject object = global_array.getJSONObject(JSONpointer);

						object.put("Fields", fieldarray);
						// System.out.println("putting " + methodarray);
						object.put("Methods", methodarray);

						global_array.put(JSONpointer, object);
					}

					// find the present class and append the Fields and Methods

				}

				global_struct.put("global_struct", global_array);

			}

			/// System.out.println(global_struct);
		} catch (Exception e) {

			e.printStackTrace();

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
