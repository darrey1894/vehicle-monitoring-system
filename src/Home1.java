




import com.digitalpersona.onetouch.DPFPCaptureFeedback;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import de.javasoft.plaf.synthetica.SyntheticaBlueLightLookAndFeel;
import java.awt.Color;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author darrey
 */
public class Home1 extends javax.swing.JFrame {
Connection conn;
PreparedStatement pstmt;
ResultSet rs;
    int y = 4;
        Variables var = new Variables();
      
private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
    public Home1() {
        super("Finger Print Enrollment Section");
        initComponents();
conn= Javaconnect.ConnecrDB();
          var.setFing(0);
        // this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				init();
				start();
			}
			public void componentHidden(ComponentEvent e) {
				stop();
			}

		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
    private DPFPEnrollment enroll = DPFPGlobal.getEnrollmentFactory().createEnrollment();
 private  DPFPTemplate template; 
     public static String TEMPLATE_PROPERTY = "template";  
	protected void init()
	{

		capturer.addDataListener(new DPFPDataAdapter() {
			public void dataAcquired(final DPFPDataEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("The fingerprint sample was captured.");
					setPrompt("Scan the next finger.");
					process(e.getSample());
				}});
			}
		});
		capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			public void readerConnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					setPrompt("Scan the Driver fingerprint.");
				}});
			}
			public void readerDisconnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					setPrompt("Connect Fingerprint Reader.");
				}});
			}
		});
		capturer.addSensorListener(new DPFPSensorAdapter() {
			public void fingerTouched(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("The fingerprint reader was touched.");
				}});
			}
			public void fingerGone(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("The finger was removed from the fingerprint reader.");
				}});
			}
		});
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			public void onImageQuality(final DPFPImageQualityEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
						makeReport("The quality of the fingerprint sample is good.");
					else
						makeReport("The quality of the fingerprint sample is poor.");
				}});
			}
		});
	}
public void setTemplate(DPFPTemplate template) {
DPFPTemplate old = this.template;
this.template = template;
firePropertyChange(TEMPLATE_PROPERTY, old, template);
}

public DPFPTemplate getTemplate() {
return template;
}
    
public DPFPFeatureSet featuresinscripcion;
public DPFPFeatureSet featuresverificacion;
	protected void process(DPFPSample sample)
	{
		// Draw fingerprint sample image.
		//drawPicture(convertSampleToBitmap(sample));
	featuresinscripcion = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT); 
featuresverificacion = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
 
if (featuresinscripcion != null){
try{
  
  y --;
                    JOptionPane ops = new JOptionPane("Please Touch the Biometrics for:" +y +" more times",JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = ops.createDialog("Please Enter your Finger");
                    dialog.setAlwaysOnTop(true); //<-- this line
                    dialog.setModal(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);

enroll.addFeatures(featuresinscripcion);  
Image image=convertSampleToBitmap(sample);
drawPicture(image);

//btnIdentificar.setEnabled(true);
}
catch (DPFPImageQualityException ex) {


 
}

finally {
 
switch(enroll.getTemplateStatus()){
case TEMPLATE_STATUS_READY:  
stop();
setTemplate(enroll.getTemplate());
 JOptionPane.showMessageDialog(null,"Finger Print Capture is Completed");
 
break;

case TEMPLATE_STATUS_FAILED:  
enroll.clear();
stop();

//EstadoHuellas();
setTemplate(null);
JOptionPane.showMessageDialog(this, "process", "process sample", JOptionPane.ERROR_MESSAGE);
start();
break;
}
}
}
        }
	protected void start()
	{
		capturer.startCapture();
		setPrompt("Scan Driver's fingerprint");
	}

	protected void stop()
	{
		capturer.stopCapture();
	}

	public void setStatus(String string) {
		status.setText(string);
	}
	public void setPrompt(String string) {
		prompt.setText(string);
	}
	public void makeReport(String string) {
//		log.append(string + "\n");
	}

	public void drawPicture(Image image) {
		picture.setIcon(new ImageIcon(
				image.getScaledInstance(picture.getWidth(), picture.getHeight(), Image.SCALE_DEFAULT)));
	}

	protected Image convertSampleToBitmap(DPFPSample sample) {
		return DPFPGlobal.getSampleConversionFactory().createImage(sample);
	}

	protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose)
	{
		DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
		try {
			return extractor.createFeatureSet(sample, purpose);
		} catch (DPFPImageQualityException e) {
			return null;
		}
	}

	//For enrollment template, use *.fpt for file format
	//For verification feature, use *.fpp
        JFileChooser jfc;FileNameExtensionFilter flt;File fl;
	protected void writeFile(String filepath, byte[] data) {
		try {
			FileOutputStream out = new FileOutputStream(new File(filepath));
			out.write(data);
			out.flush();
		out.close();
                       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * For patient with bad/missing fingerprints, we ask the clerk to enter a validation number 
	 * to skip the procedure (to punish lazy clerks who don't want to enter fingerprints).
	 */
	
	public static void notifyHTML(String result) {
	//	if(jso!=null)
	//		jso.call("updateFingerprintStatus", new String[] {result} );
	}
     
    

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        log = new javax.swing.JLabel();
        prompt = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        picture = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        car_platenum = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        car_type = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        ID_NUM = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(891, 660));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        log.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        log.setForeground(new java.awt.Color(0, 255, 0));
        jPanel3.add(log, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 640, 280, 30));

        prompt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        prompt.setForeground(new java.awt.Color(0, 255, 0));
        jPanel3.add(prompt, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 640, 280, 28));

        status.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        status.setForeground(new java.awt.Color(0, 255, 0));
        jPanel3.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 280, 25));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/House_48px.png"))); // NOI18N
        jButton3.setText("LOG OUT");
        jButton3.setToolTipText("Home Page");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 650, 160, 40));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("ENROLLMENT PAGE");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 40)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("FINGERPRINT");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iStock_fingerprint31.jpg"))); // NOI18N
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -40, 730, 630));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 20, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PASSPORT");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 230, 90, 20));

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picture, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picture, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FINGER PRINT ");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 230, 130, 20));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 290, 290, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("GENDER");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 500, 100, -1));

        buttonGroup1.add(male);
        male.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        male.setForeground(new java.awt.Color(255, 255, 0));
        male.setText("Male");
        male.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maleMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                maleMousePressed(evt);
            }
        });
        jPanel3.add(male, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 530, 80, -1));

        buttonGroup1.add(female);
        female.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        female.setForeground(new java.awt.Color(255, 255, 0));
        female.setText("Female");
        female.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                femaleMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                femaleMousePressed(evt);
            }
        });
        jPanel3.add(female, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 530, 93, -1));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setForeground(new java.awt.Color(204, 255, 255));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Return_48px.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Save_24px.png"))); // NOI18N
        jButton1.setText("SAVE PERSON DATA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        car_platenum.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        car_platenum.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        car_platenum.setText("XC234CF");
        car_platenum.setToolTipText("Not more that seven character,UPPERCASE Character ONLY.");
        car_platenum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_platenumActionPerformed(evt);
            }
        });
        car_platenum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_platenumKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CAR PLATE NUMBER");

        car_type.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        car_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_typeActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("CAR TYPE");

        ID_NUM.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ID_NUM.setToolTipText("Not more that six character, Start with UPPERCASE Character.");
        ID_NUM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_NUMActionPerformed(evt);
            }
        });
        ID_NUM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ID_NUMKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ID_NUMKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID NUMBER");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DRIVER NAME");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(car_platenum, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(car_type, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ID_NUM, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 300, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ID_NUM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(car_type, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(car_platenum, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 0, 450, 590));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 700));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1213)/2, (screenSize.height-739)/2, 1213, 739);
    }// </editor-fold>//GEN-END:initComponents
  
    
    

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
         //   int tamanoHuella = template.serialize().length;
      String sql = "INSERT INTO enroll (NAME , FINGER ,IMAGE, DRIVER_ID ,CAR_TYPE ,PLATE_NO ) VALUES (?,?,?,?,?,?)";
            try{
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, jTextField1.getText());
        pstmt.setBinaryStream(2,datosHuella,(template.serialize().length));
      //  pstmt.setBinaryStream(1,new ByteArrayInputStream(template.getData()), template.getData().length);
          pstmt.setBytes(3, person_image);
    
            pstmt.setString(4, ID_NUM.getText());
            pstmt.setString(5, car_type.getText());
            pstmt.setString(6, car_platenum.getText());
            
          pstmt.execute();
          JOptionPane.showMessageDialog(null,"NEW DRIVER SAVED");
      }catch(Exception e){
          JOptionPane.showMessageDialog(null , e);
      }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
         JFileChooser chooser = new JFileChooser();
      chooser.showOpenDialog(this);
      
        File f = chooser.getSelectedFile();
      
        filename = f.getAbsolutePath();
     //  path.setText(filename);
       String path  = f.getAbsolutePath();
ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));
 jLabel2.setIcon(icon);
   
        try{
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte [1024];
            for (int readNum; (readNum = fis.read(buf))!=-1;){
             bos.write(buf,0,readNum);   
            }
            person_image = bos.toByteArray();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null , "PICTURE TOO LARGE");
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        securitypost c = new securitypost();
        c.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ID_NUMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_NUMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_NUMActionPerformed

    private void maleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maleMouseClicked
        // TODO add your handling code here:
        //gender ="Male";
    }//GEN-LAST:event_maleMouseClicked

    private void femaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_femaleMouseClicked
        // TODO add your handling code here:
        //gender = "Female";
    }//GEN-LAST:event_femaleMouseClicked

    private void maleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maleMousePressed
        //gender ="Male";
    }//GEN-LAST:event_maleMousePressed

    private void femaleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_femaleMousePressed
        // TODO add your handling code here:
        //gender ="Female";
    }//GEN-LAST:event_femaleMousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        security c = new security();
        c.setVisible(true);
          
    }//GEN-LAST:event_jButton3ActionPerformed

    private void car_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_typeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_car_typeActionPerformed

    private void car_platenumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_platenumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_car_platenumActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(!(Character.isLetter(c) || (c==KeyEvent.VK_BACK_SPACE)||(c==KeyEvent.VK_DELETE) || (c==KeyEvent.VK_SPACE))){
        getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void ID_NUMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ID_NUMKeyPressed
        // TODO add your handling code h
    }//GEN-LAST:event_ID_NUMKeyPressed

    private void ID_NUMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ID_NUMKeyTyped
        // TODO add your handling code here:
        
        char key=evt.getKeyChar();
        if(!(Character.isLetter(key)&&Character.isUpperCase(key) ||(Character.isDigit(key))||(key==KeyEvent.VK_BACK_SPACE)||(key==KeyEvent.VK_DELETE)))
        {
           getToolkit().beep();
            evt.consume();
        } 
        if(!(ID_NUM.getText().length()<=5)){
            evt.consume();
        }
    }//GEN-LAST:event_ID_NUMKeyTyped

    private void car_platenumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_platenumKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(!(Character.isLetter(c)&&Character.isUpperCase(c) ||(Character.isDigit(c))||(c==KeyEvent.VK_BACK_SPACE)||(c==KeyEvent.VK_DELETE)))
        {
           getToolkit().beep();
            evt.consume();
        } 
        if(!(car_platenum.getText().length()<=6)){
            getToolkit().beep();
            evt.consume();
        }
        
    }//GEN-LAST:event_car_platenumKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
     try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Clocking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clocking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clocking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clocking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ID_NUM;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField car_platenum;
    private javax.swing.JTextField car_type;
    private javax.swing.JRadioButton female;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel log;
    private javax.swing.JRadioButton male;
    private javax.swing.JLabel picture;
    private javax.swing.JLabel prompt;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables

    private String gender;
private ImageIcon format = null;    
    String filename = null;
    int s = 0;
    byte[] person_image = null;
}
