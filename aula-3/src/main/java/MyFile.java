import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Eng. Porfírio Filipe
 *
 */
// manipulação de caminhos 
class MyFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private String relativePath = null;
	private char pathSeparator = File.separatorChar;
	private char extensionSeparator = '.';

	public MyFile(String str) {
		relativePath = str;
	}

	public MyFile(String str, char sep, char ext) {
		relativePath = str;
		pathSeparator = sep;
		extensionSeparator = ext;
	}

	/**
	 * Devolve a pasta de contexto do projeto corrente
	 * @return
	 */
	public static final String getContext() {
		String contexto = "WebContent";
		File f = new File(contexto);
		if (!(f.exists() && f.isDirectory())) {
			contexto = "src/main/webapp";
		}
		return contexto;
	}
	
	public String getAbsolutePath() {
		try {
			// Create a file object and get the absolute path
			return new File(relativePath).getAbsolutePath();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return "";
	}
	public String getExtension() {
		int dot = relativePath.lastIndexOf(extensionSeparator);
		if(dot==-1)
			return "";
		return relativePath.substring(dot + 1);
	}

	public String getName() { // gets filename without extension
		int dot = relativePath.lastIndexOf(extensionSeparator);
		int sep = relativePath.lastIndexOf(pathSeparator);
		if(dot==-1 && sep==-1)
			return relativePath;
		if(dot==-1)
			return relativePath.substring(sep + 1);
		return relativePath.substring(sep + 1,dot);
	}

	public String getFName() { // gets filename with extension
		String ext=getExtension();
		if (ext.length()==0)
		  return getName();
		return getName()+extensionSeparator+ext;
	}
	public String getRPath() {
		int sep = relativePath.lastIndexOf(pathSeparator);
		if(sep==-1)
			return "";
		return relativePath.substring(0, sep);
	}
	
	public String getPath() {
		String ext=getExtension();
		if(ext.length()==0)
			return getContext()+pathSeparator+getRPath()+pathSeparator+getName();
		return getContext()+pathSeparator+getRPath()+pathSeparator+getName()+extensionSeparator+ext;
	}
	
	public String getMimeType() {
		return getMimeType(getExtension());
	}

	// apresenta um ArrayList com os ficheiros existentes numa pasta
	public static final ArrayList<String> listarFicheiros(String pasta) {
		ArrayList<String> result = new ArrayList<String>(); // Create an ArrayList of filenames
		File folder = new File(getContext()+File.separatorChar+pasta);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile() /*&& listOfFiles[i].getName().toLowerCase().endsWith(".xml")*/)
				result.add(listOfFiles[i].getName());
		return result;
	}
	
	public static final String getMimeType(String fileExtension) {
		switch (fileExtension) {
		case "txt":
			return "text/plain";
		case "gif":
			return "image/gif";
		case "jpg":
		case "jpeg":
			return "image/jpeg";
		case "bmp":
			return "image/bmp";
		case "png":
			return "image/png";
		case "wav":
			return "audio/wav";
		case "mp4":
			return "video/mp4";
		case "ogv":
			return "video/ogg";
		case "oga":
			return "audio/ogg";
		case "ogx":
			return "application/ogg";
		case "mp3":
			return "audio/mpeg";
		default:
			return "application/octet-stream";
		}
	}

	public void show () {
		System.out.println("AbsolutPath = "+getAbsolutePath());
		System.out.println("ProjetPath = " + getPath());
		System.out.println("ContextPath = " + getContext());
		System.out.println("RelativePath = " + getRPath());
		System.out.println("Filename = " + getName());
		System.out.println("Extension = " + getExtension());
		System.out.println("MimeType = " + getMimeType());
	}
	
	public static void main(String[] args) {
		final String FPATH = "fotos/isel.jpg";
		MyFile myHomePage = new MyFile(FPATH, '/', '.');
		myHomePage.show();
	}
}
