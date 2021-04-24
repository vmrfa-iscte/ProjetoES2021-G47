package G47.Grupo47;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.SelectionListener;
import java.util.function.Consumer;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.GestureEvent;

public class mainGUI extends Shell {

	private int indice,i;
	private List ficheirosexcel;
	private Text foldername,txtNmeroDeMtodos,NumClasses,txtNmeroDeClasses,txtNmeroDeLinhas,txtNmeroDePackages,NumPackages,NumMethods,NumLines,text_1,limite_1,limite_2,limite_3;
	private StyledText styledText;
	private Composite composite;
	private Button btnDefinirRegras,btnSelecionarFicheirohistrico,guardarhistorico;
	private String diretoria,nomepath = new String();
	private File selectedFile1 = null;
	private String nameFile,pathToExtract = "";
	private HashMap<String,ArrayList<String>> mapStats = new HashMap<>();
	private String username = System.getProperty("user.name");
	private File rules,pastaselecionada,historico;
	private FileWriter fw;
	private BufferedWriter bw;
	private ArrayList<Rules> list = new ArrayList<Rules>();
	private Rules rule, currentRule;
	private String[] selected_rule;
	private ArrayList<MethodMetrics> actualmetrics;
	private Label lblDefinaUmaRegra;
	private Combo metrica3,sinal,sinal2,sinal3,operador2;
	private String content,update;
	private Combo metrica2;
	private Text folderToExtract;
	private File folderextraction = null;
	private static final int ZERO = 0, ONE = 1, TWO = 2, THREE = 3, FOUR = 4, FIVE = 5, SIX = 6, SEVEN = 7, EIGHT = 8, NINE = 9;
	private static final String LOGO = "/G47/Grupo47/iscte_logo2.jpg", GUI_NAME = "Interface gráfica- Grupo 47";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public mainGUI(Display display) {
		super(display, SWT.SHELL_TRIM);
		setMinimumSize(new Point(170, 47));
		setImage(SWTResourceManager.getImage(mainGUI.class, LOGO));

		setLayout(null);

		foldername = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		foldername.setBounds(10, 67, 345, 26);

		Button pasta = new Button(this, SWT.NONE);
		pasta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser pathpasta = new JFileChooser(".");
				pathpasta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = pathpasta.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					selectedFile1 = pathpasta.getSelectedFile();
					nomepath = selectedFile1.getAbsolutePath();
				}
				if(selectedFile1 != null) {
				foldername.setText(selectedFile1.getPath());
				}
			}
		});
		pasta.setBounds(372, 67, 166, 28);
		pasta.setText("Selecionar projeto (src)");

		ficheirosexcel = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		ficheirosexcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				indice = ficheirosexcel.getSelectionIndex();
				System.out.println("indice: " + indice);

				if (indice != -1) {
					for (Entry<String, ArrayList<String>> entry : mapStats.entrySet()) {
						System.out.println(ficheirosexcel.getItem(indice));
						if (entry.getKey().equals(ficheirosexcel.getItem(indice))) {
							ArrayList<String> statsToWrite = entry.getValue();
							NumLines.setText(statsToWrite.get(0));
							NumClasses.setText(statsToWrite.get(2));
							NumMethods.setText(statsToWrite.get(1));
							NumPackages.setText(statsToWrite.get(3));
							DirExplorer dirEx = new DirExplorer(selectedFile1);
							try {
								actualmetrics = dirEx.exploreAndExtract();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}

				}

			}
		});

		ficheirosexcel.setBounds(10, 108, 345, 164);

		composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 314, 670, 465);

		txtNmeroDeMtodos = new Text(this, SWT.BORDER);
		txtNmeroDeMtodos.setEditable(false);
		txtNmeroDeMtodos.setText("Número de Métodos");
		txtNmeroDeMtodos.setBounds(372, 198, 166, 26);

		NumClasses = new Text(this, SWT.BORDER);
		NumClasses.setEditable(false);
		NumClasses.setBounds(544, 109, 136, 26);

		txtNmeroDeClasses = new Text(this, SWT.BORDER);
		txtNmeroDeClasses.setEditable(false);
		txtNmeroDeClasses.setText("Número de Classes");
		txtNmeroDeClasses.setBounds(372, 109, 166, 26);

		txtNmeroDeLinhas = new Text(this, SWT.BORDER);
		txtNmeroDeLinhas.setEditable(false);
		txtNmeroDeLinhas.setText("Número de Linhas");
		txtNmeroDeLinhas.setBounds(372, 246, 166, 26);

		txtNmeroDePackages = new Text(this, SWT.BORDER);
		txtNmeroDePackages.setEditable(false);
		txtNmeroDePackages.setText("Número de Packages");
		txtNmeroDePackages.setBounds(372, 152, 166, 26);

		NumPackages = new Text(this, SWT.BORDER);
		NumPackages.setEditable(false);
		NumPackages.setBounds(544, 152, 136, 26);

		NumMethods = new Text(this, SWT.BORDER);
		NumMethods.setEditable(false);
		NumMethods.setBounds(544, 198, 136, 26);

		NumLines = new Text(this, SWT.BORDER);
		NumLines.setEditable(false);
		NumLines.setBounds(544, 246, 136, 26);

		Button extrair = new Button(this, SWT.NONE);
		extrair.setBounds(544, 48, 136, 30);
		extrair.setText("Extrair métricas");
		extrair.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedFile1 == null || folderextraction == null ) {
					JOptionPane.showMessageDialog(null, "Escolha uma pasta");
				}else {
					DirExplorer dirEx = new DirExplorer(selectedFile1);
					try {
						if(folderextraction.exists() && selectedFile1.exists()) {
							NameByFile excelFileName = new NameByFile();
							excelFileName.setFileToExtract(selectedFile1);
							actualmetrics = dirEx.exploreAndExtract();
							System.out.println("actualmetrics size: " + actualmetrics.size());
							System.out.println(pathToExtract);
							ExcelManip em = new ExcelManip(selectedFile1);
							em.createExcel(actualmetrics,folderToExtract.getText());
							Statistics stats = new Statistics(actualmetrics);
							ArrayList<String> StringStats = new ArrayList<>();
							StringStats.add(String.valueOf(stats.countLinesOfCode()));
							StringStats.add(String.valueOf(stats.countNumberOfMethods()));
							StringStats.add(String.valueOf(stats.countClasses()));
							StringStats.add(String.valueOf(stats.countPackages()));
							System.out.println("em.getFileName(): " + excelFileName.getFileName());
							mapStats.put(excelFileName.getFileName(), StringStats);
							ficheirosexcel.add(excelFileName.getFileName());
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}



				}
			}
		});

		Combo metrica1 = new Combo(composite, SWT.READ_ONLY);
		metrica1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SelectionIndex: " + metrica1.getSelectionIndex());
				if (metrica1.getSelectionIndex() != -1) {
					System.out.println(metrica1.getItem(metrica1.getSelectionIndex()));
					if (metrica1.getItem(metrica1.getSelectionIndex()).equals("LOC_method")) {
						operador2.setVisible(false);
						sinal3.setVisible(false);
						metrica3.setVisible(false);
						limite_3.setVisible(false);
						metrica2.removeAll();
						metrica2.add("CYCLO_method");

					} else if(metrica1.getItem(metrica1.getSelectionIndex()).equals("WMC_class")) {
						metrica2.removeAll();
						metrica2.add("NOM_class");
						metrica2.add("LOC_class");
						operador2.setVisible(true);
						sinal3.setVisible(true);
						metrica3.setVisible(true);
						limite_3.setVisible(true);
					}else if(metrica1.getItem(metrica1.getSelectionIndex()).equals("NOM_class")) {
						operador2.setVisible(false);
						sinal3.setVisible(false);
						metrica3.setVisible(false);
						limite_3.setVisible(false);
						metrica2.removeAll();
						metrica2.add("LOC_class");
					}
				}
			}
		});
		metrica1.setBounds(10, 58, 155, 28);
		metrica1.setText("");
		metrica1.add("LOC_method");
		metrica1.add("WMC_class");
		metrica1.add("NOM_class");

		Combo operador = new Combo(composite, SWT.READ_ONLY);
		operador.setBounds(440, 58, 117, 28);
		operador.setText("");
		operador.add("OR");
		operador.add("AND");
		
		metrica2 = new Combo(composite, SWT.READ_ONLY);
		metrica2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(metrica2.getSelectionIndex() != -1) {
					if(metrica2.getItem(metrica2.getSelectionIndex()).equals("LOC_class")) {
						operador2.setVisible(false);
						sinal3.setVisible(false);
						metrica3.setVisible(false);
						limite_3.setVisible(false);
					}else if(metrica2.getItem(metrica2.getSelectionIndex()).equals("NOM_class")) {
						operador2.setVisible(true);
						sinal3.setVisible(true);
						metrica3.setVisible(true);
						limite_3.setVisible(true);
					}
				}
			}
		});
		metrica2.setBounds(10, 92, 155, 28);
		metrica2.setText("");

		metrica3 = new Combo(composite, SWT.READ_ONLY);
		metrica3.setBounds(10, 126, 155, 28);
		metrica3.setText("");
		metrica3.add("");
		metrica3.add("LOC_class");
		metrica3.setVisible(false);
		
		limite_1 = new Text(composite, SWT.BORDER);
		limite_1.setBounds(313, 58, 94, 30);
		limite_1.setText("Limite");

		limite_2 = new Text(composite, SWT.BORDER);
		limite_2.setBounds(313, 92, 94, 28);
		limite_2.setText("Limite");

		List regras = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		regras.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				i = regras.getSelectionIndex();
				currentRule = list.get(i);
				metrica1.setText(currentRule.getMethod1());
				sinal.setText(currentRule.getSinal1());
				limite_1.setText(currentRule.getLimit1());
				operador.setText(currentRule.getOperator());
				metrica2.setText(currentRule.getMethod2());
				sinal2.setText(currentRule.getSinal2());
				limite_2.setText(currentRule.getLimit2());
				operador2.setText(currentRule.getOperator2());
				metrica3.setText(currentRule.getMethod3());
				sinal3.setText(currentRule.getSinal3());
				limite_3.setText(currentRule.getLimit3());
			}
		});
		regras.setLocation(10, 186);
		regras.setSize(431, 230);

		btnDefinirRegras = new Button(composite, SWT.NONE);
		btnDefinirRegras.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("limite_1 "+limite_1.getText()+"   limite_2 "+limite_2.getText());
				if (!isValid(limite_1.getText()) || !isValid(limite_2.getText()) ) {
					JOptionPane.showMessageDialog(null, "Limites inválidos!");
				} else {
					if((metrica3.getText().isEmpty() && operador2.getText().isEmpty() && (limite_3.getText().isEmpty() || limite_3.getText().equals("Limite")) && sinal3.getText().isEmpty()) || (!metrica3.getText().isEmpty() && !operador2.getText().isEmpty() && !(limite_3.getText().isEmpty() || limite_3.getText().equals("Limite")) && !sinal3.getText().isEmpty())){

						boolean v = false;
						if (!metrica1.getText().isEmpty() && !operador.getText().isEmpty() && !metrica2.getText().isEmpty()
								&& !limite_2.getText().isEmpty() && !limite_1.getText().isEmpty()) {
							if (!metrica3.getText().isEmpty() && !operador2.getText().isEmpty()
									&& !limite_3.getText().isEmpty() && !sinal3.getText().isEmpty()) {
								rule = new Rules(metrica1.getText(), sinal.getText(), limite_1.getText(),
										operador.getText(), metrica2.getText(), sinal2.getText(), limite_2.getText(),
										operador2.getText(), metrica3.getText(), sinal3.getText(), limite_3.getText());
								content = rule.toString();
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i).toString().contentEquals(rule.toString())) {
										JOptionPane.showMessageDialog(null, "Regra já imposta.");
										v = true;
										break;

									}
								}
							} else {
								rule = new Rules(metrica1.getText(), sinal.getText(), limite_1.getText(),
										operador.getText(), metrica2.getText(), sinal2.getText(), limite_2.getText(), "",
										"", "", "");
								content = rule.toString();
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i).toString().contentEquals(rule.toString())) {
										JOptionPane.showMessageDialog(null, "Regra já imposta.");
										v = true;
										break;

									}
								}
							}

							System.out.println(content);
							if (v == false) {
								regras.add(content);
								list.add(rule);
								System.out.println(list.size());
							}

						} else {
							JOptionPane.showMessageDialog(null, "Preencha corretamente todos os campos.");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Preencha todos os campos");
					}
				}

			}

		});

		btnDefinirRegras.setBounds(447, 185, 142, 30);
		btnDefinirRegras.setText("Definir regra");

		Button alterarregra = new Button(composite, SWT.NONE);
		alterarregra.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isValid(limite_1.getText()) || !isValid(limite_2.getText())) {
					JOptionPane.showMessageDialog(null, "Limites inválidos!");
				} else {
					if((metrica3.getText().isEmpty() && operador2.getText().isEmpty() && (limite_3.getText().isEmpty() || limite_3.getText().equals("Limite")) && sinal3.getText().isEmpty()) || (!metrica3.getText().isEmpty() && !operador2.getText().isEmpty() && !(limite_3.getText().isEmpty() || limite_3.getText().equals("Limite")) && !sinal3.getText().isEmpty())){
						if (regras.isSelected(i)) {
							System.out.println(list.get(i).toString());
							list.get(i).setLimit1(limite_1.getText());
							list.get(i).setLimit2(limite_2.getText());
							list.get(i).setLimit3(limite_3.getText());
							list.get(i).setMethod1(metrica1.getText());
							list.get(i).setMethod2(metrica2.getText());
							list.get(i).setMethod3(metrica3.getText());
							list.get(i).setOperator(operador.getText());
							list.get(i).setOperator2(operador2.getText());
							list.get(i).setSinal1(sinal.getText());
							list.get(i).setSinal2(sinal2.getText());
							list.get(i).setSinal3(sinal3.getText());
							System.out.println(list.get(i).toString());
							System.out.println(regras.getItem(i));
							update = list.get(i).toString();
							for (int x = 0; x < list.size(); x++) {
								if (x == i) {
									regras.remove(x);
									regras.add(update, x);

								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "Nenhuma regra selecionada");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Preencha corretamente todos os campos");
					}

				}
			}
		});
		;

		alterarregra.setBounds(447, 224, 145, 30);
		alterarregra.setText("Alterar regras");

		Button codesmells = new Button(composite, SWT.NONE);
		codesmells.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(actualmetrics == null) {
					JOptionPane.showMessageDialog(null, "Não foram extraidas métricas");
					
				}else {
				if (regras.isSelected(i)) {
					String method1 = currentRule.getMethod1();
					String method2 = currentRule.getMethod2();
					String signal1 = currentRule.getSinal1();
					int limit1 = Integer.parseInt(currentRule.getLimit1());
					String operator = currentRule.getOperator();
					String signal2 = currentRule.getSinal2();
					int limit2 = Integer.parseInt(currentRule.getLimit2());
					if (method1.equals("LOC_method")) {
						evaluateLocMethod(signal1,signal2,limit1,limit2,operator);
					}
					if (method1.equals("WMC_class") && method2.equals("NOM_class") && !currentRule.getMethod3().contains("a")) {
						evaluateGodClassWithWMC_NOM(signal1,signal2,limit1,limit2,operator);
					} 
					
					if (method1.equals("WMC_class") && method2.equals("LOC_class") && !currentRule.getMethod3().contains("a")) {
						evaluateGodClassWithWMC_LOC(signal1,signal2,limit1,limit2,operator);
					}
					
					if (method1.equals("NOM_class") && method2.equals("LOC_class") && !currentRule.getMethod3().contains("a")) {
						evaluateGodClassWithNOM_LOC(signal1,signal2,limit1,limit2,operator);
					}
										
					if(method1.equals("WMC_class") && currentRule.getMethod3().contains("a")) {
						String operator2 = currentRule.getOperator2();
						String signal3 = currentRule.getSinal3();
						int limit3 = Integer.parseInt(currentRule.getLimit3());
						evaluateGodClassWithWMC_NOM_LOC(signal1,signal2,signal3,limit1,limit2,limit3,operator,operator2);
					}

				} else {
					JOptionPane.showMessageDialog(null, "Nenhuma regra selecionada");
				}
				
			}
			}

		});

		Button carregarhist = new Button(composite, SWT.NONE);
		carregarhist.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				regras.removeAll();
				list.clear();
				System.out.println(list.size());
				JFileChooser pathpasta = new JFileChooser(".txt");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				pathpasta.setFileFilter(filter);
				pathpasta.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnValue = pathpasta.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					historico = pathpasta.getSelectedFile();

				}
				if(historico == null) {
				}else {
					if (historico.getPath().endsWith(".txt")) {
						try {
						FileReader reader = new FileReader(new File(historico.getPath()));
						BufferedReader bufferedReader = new BufferedReader(reader);
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							System.out.println(line);
							String[] rules = line.split(" ");
							for (int i = 0; i < rules.length; i++) {
								System.out.println(rules[i]);
							}
							if (rules.length > 9) {
								Rules x = new Rules(rules[0], rules[1], rules[2], rules[3], rules[4], rules[5],
										rules[6], rules[7], rules[8], rules[9], rules[10]);
								list.add(x);
								regras.add(line);
							} else {
								Rules x = new Rules(rules[0], rules[1], rules[2], rules[3], rules[4], rules[5],
										rules[6], "", "", "", "");
								list.add(x);
								regras.add(line);
							}

						}
						reader.close();
						} catch (FileNotFoundException e1) {

							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Ficheiro inválido");
					}
				}

			}

		});

		carregarhist.setBounds(10, 422, 212, 30);
		carregarhist.setText("Carregar histórico de regras");

		codesmells.setBounds(451, 422, 167, 30);
		codesmells.setText("Deteção de codesmells");

		Label lblRegrasGuardadas = new Label(composite, SWT.NONE);
		lblRegrasGuardadas.setBounds(10, 160, 155, 20);
		lblRegrasGuardadas.setText("Regras guardadas:");

		lblDefinaUmaRegra = new Label(composite, SWT.NONE);
		lblDefinaUmaRegra.setText("Defina/altere uma regra para a deteção de codesmells: ");
		lblDefinaUmaRegra.setBounds(10, 32, 397, 20);

		guardarhistorico = new Button(composite, SWT.NONE);
		guardarhistorico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!list.isEmpty()) {
					SaveHistoryGUI hist = new SaveHistoryGUI(display, regras, list);
					hist.loadGUI();
				} else {
					JOptionPane.showMessageDialog(null, "Lista de regras vazia");
				}
			}
		});
		guardarhistorico.setBounds(227, 422, 214, 30);
		guardarhistorico.setText("Guardar histórico");

		sinal = new Combo(composite, SWT.READ_ONLY);
		sinal.setBounds(196, 58, 80, 28);
		sinal.add(">");
		sinal.add("<");

		operador2 = new Combo(composite, SWT.READ_ONLY);
		operador2.setBounds(440, 92, 117, 28);
		operador2.setText("");
		operador2.add("OR");
		operador2.add("AND");
		operador2.add(" ");
		operador2.setVisible(false);

		sinal3 = new Combo(composite, SWT.READ_ONLY);
		sinal3.setBounds(196, 126, 80, 28);
		sinal3.add(">");
		sinal3.add("<");
		sinal3.add(" ");
		sinal3.setVisible(false);

		limite_3 = new Text(composite, SWT.BORDER);
		limite_3.setText("Limite");
		limite_3.setBounds(313, 126, 94, 28);
		limite_3.setVisible(false);

		sinal2 = new Combo(composite, SWT.READ_ONLY);
		sinal2.setBounds(195, 92, 81, 28);
		sinal2.add(">");
		sinal2.add("<");
		

		Label lblProjetoJavaescolha = new Label(this, SWT.NONE);
		lblProjetoJavaescolha.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblProjetoJavaescolha.setBounds(10, 9, 528, 20);
		lblProjetoJavaescolha.setText("Escolha o projeto java que pretende analisar:");

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem mntmAjuda = new MenuItem(menu, SWT.CASCADE);
		mntmAjuda.setText("Ajuda");

		Menu menu_1 = new Menu(mntmAjuda);
		mntmAjuda.setMenu(menu_1);

		MenuItem mntmUtilizaoDaInterface = new MenuItem(menu_1, SWT.NONE);
		mntmUtilizaoDaInterface.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HelpInterface hi = new HelpInterface(display);
				hi.loadGUI();

			}
		});
		mntmUtilizaoDaInterface.setText("Utilização da interface");

		MenuItem mntmInformaoSobreMtricas = new MenuItem(menu_1, SWT.NONE);
		mntmInformaoSobreMtricas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HelpGUIMetrics hgm = new HelpGUIMetrics(display);
				hgm.loadGUI();

			}
		});
		mntmInformaoSobreMtricas.setText("Informação sobre métricas");
		
		Button btnVerFicheiro = new Button(this, SWT.NONE);
		btnVerFicheiro.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(actualmetrics!=null) {
					ShowExcelGUI showGUI = new ShowExcelGUI(display,"Visualizar ficheiro",actualmetrics);
					showGUI.loadGUI();
				}else {
					JOptionPane.showMessageDialog(null, "Escolha um projeto e extraia métricas");
				}
			}
		});
		btnVerFicheiro.setBounds(10, 278, 109, 30);
		btnVerFicheiro.setText("Ver ficheiro");
		
		folderToExtract = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		folderToExtract.setBounds(10, 35, 345, 26);
		
		
		Button choosePathToExtract = new Button(this, SWT.NONE);
		choosePathToExtract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser pathpasta = new JFileChooser(".");
				pathpasta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = pathpasta.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					folderextraction = pathpasta.getSelectedFile();
					
				}
				if(folderextraction != null) {
				folderToExtract.setText(folderextraction.getPath());
				pathToExtract = folderextraction.getPath();
				
				}
		
			}
		});
		choosePathToExtract.setBounds(372, 35, 166, 28);
		choosePathToExtract.setText("Selecionar destino");
		createContents();
	}
	
	

	private boolean isValid(String text) {
		System.out.println("text: " + text);
		for (int i = 0; i < text.length(); i++) {
			System.out.println(text.charAt(i));
			if (text.charAt(i) == '0' || text.charAt(i) == '1' || text.charAt(i) == '2' || text.charAt(i) == '3'
					|| text.charAt(i) == '4' || text.charAt(i) == '5' || text.charAt(i) == '6' || text.charAt(i) == '7'
					|| text.charAt(i) == '8' || text.charAt(i) == '9') {
				System.out.println("true");
				return true;
			}
		}
		return false;
	}
	
	private void fillSecondaryGUI(ArrayList<HasCodeSmell> toFill, SecondaryGUI results) {
		for (HasCodeSmell hascodesmell : toFill) {
//			System.out.println("ID: " + hascodesmell.getMethod_ID());
			results.addCodeSmellsInfo(hascodesmell, false);
		}
	}

	private void evaluateLocMethod(String signal1, String signal2, int limit1, int limit2, String operator) {
		CodeSmellsDetector detector = new CodeSmellsDetector(limit1, limit2, operator, actualmetrics);
		ArrayList<HasCodeSmell> hcsList = new ArrayList<>();
		if (signal1.equals(">") && signal2.equals(">")) hcsList = detector.detectLongMethodBiggerBigger();
		else if(signal1.equals(">") && signal2.equals("<")) hcsList = detector.detectLongMethodBiggerSmaller();
		else if(signal1.equals("<") && signal2.equals(">")) hcsList = detector.detectLongMethodBiggerSmaller();
		else if(signal1.equals("<") && signal2.equals("<")) hcsList = detector.detectLongMethodSmallerSmaller();
		SecondaryGUI codesmells = new SecondaryGUI(getDisplay(), "IsLong Method Detection", hcsList);
		fillSecondaryGUI(hcsList,codesmells);
		codesmells.loadGUI();	
	}
	
	private void evaluateGodClassWithWMC_NOM(String signal1, String signal2, int limit1, int limit2, String operator) {
		ArrayList<HasCodeSmell> hcsList2 = new ArrayList<>();
		CodeSmellsDetector detector2 = new CodeSmellsDetector(limit1, limit2, operator, actualmetrics);
		if (signal1.equals(">") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassBiggerBiggerWMC_NOM();
		else if (signal1.equals("<") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassSmallerSmallerWMC_NOM();
		else if (signal1.equals(">") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassBiggerSmallerWMC_NOM();
		else if (signal1.equals("<") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassSmallerBiggerWMC_NOM();
		SecondaryGUI codesmells2 = new SecondaryGUI(getDisplay(), "IsGod Class Detection", hcsList2);
		fillSecondaryGUI(hcsList2,codesmells2);
		codesmells2.loadGUI();
	}
	
	private void evaluateGodClassWithWMC_LOC(String signal1, String signal2, int limit1, int limit2, String operator) {
		ArrayList<HasCodeSmell> hcsList2 = new ArrayList<>();
		CodeSmellsDetector detector2 = new CodeSmellsDetector(limit1, limit2, operator, actualmetrics);
		if (signal1.equals(">") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassBiggerBiggerWMC_LOC();
		else if (signal1.equals("<") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassSmallerSmallerWMC_LOC();
		else if (signal1.equals(">") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassBiggerSmallerWMC_LOC();
		else if (signal1.equals("<") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassSmallerBiggerWMC_LOC();
		SecondaryGUI codesmells2 = new SecondaryGUI(getDisplay(), "IsGod Class Detection", hcsList2);
		fillSecondaryGUI(hcsList2,codesmells2);
		codesmells2.loadGUI();
	}
	
	private void evaluateGodClassWithNOM_LOC(String signal1, String signal2, int limit1, int limit2, String operator) {
		ArrayList<HasCodeSmell> hcsList2 = new ArrayList<>();
		CodeSmellsDetector detector2 = new CodeSmellsDetector(limit1, limit2, operator, actualmetrics);
		if (signal1.equals(">") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassBiggerBiggerNOM_LOC();
		else if (signal1.equals("<") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassSmallerSmallerNOM_LOC();
		else if (signal1.equals(">") && signal2.equals("<") ) hcsList2 = detector2.detectGodClassBiggerSmallerNOM_LOC();
		else if (signal1.equals("<") && signal2.equals(">") ) hcsList2 = detector2.detectGodClassSmallerBiggerNOM_LOC();
		SecondaryGUI codesmells2 = new SecondaryGUI(getDisplay(), "IsGod Class Detection", hcsList2);
		fillSecondaryGUI(hcsList2,codesmells2);
		codesmells2.loadGUI();
	}
	
	private void evaluateGodClassWithWMC_NOM_LOC(String signal1,String signal2,String signal3,int limit1,int limit2,int limit3,String operator,String operator2) {
		ArrayList<HasCodeSmell> hcslist = new ArrayList<>();
		CodeSmellsDetector detector = new CodeSmellsDetector(limit1,limit2,limit3,operator,operator2,actualmetrics);
			if (signal1.equals(">") && signal2.equals(">") && signal3.equals(">")) hcslist = detector.detectGodClassBiggerBiggerBigger();
			else if (signal1.equals("<") && signal2.equals("<") && signal3.equals("<")) hcslist = detector.detectGodClassSmallerSmallerSmaller();
			else if (signal1.equals(">") && signal2.equals("<") && signal3.equals(">")) hcslist = detector.detectGodClassBiggerSmallerSmaller();
			else if (signal1.equals(">") && signal2.equals("<") && signal3.equals(">")) hcslist = detector.detectGodClassBiggerSmallerBigger();
			else if (signal1.equals(">") && signal2.equals(">") && signal3.equals("<")) hcslist = detector.detectGodClassBiggerBiggerSmaller();
			else if (signal1.equals("<") && signal2.equals("<") && signal3.equals(">")) hcslist = detector.detectGodClassSmallerSmallerBigger();
			else if (signal1.equals("<") && signal2.equals(">") && signal3.equals(">")) hcslist = detector.detectGodClassSmallerBiggerBigger();
			else if (signal1.equals("<") && signal2.equals(">") && signal3.equals("<")) hcslist = detector.detectGodClassSmallerBiggerSmaller();
			SecondaryGUI codesmells = new SecondaryGUI(getDisplay(), "IsGod Class Detection", hcslist);
			fillSecondaryGUI(hcslist,codesmells);
			codesmells.loadGUI();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(GUI_NAME);
		setSize(726, 861);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
