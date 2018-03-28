import java.io.*;

public class ObjectCreator<T> {

    private T object;
    private File file;

    public void setObject(T object) {
        this.object = object;
    }

    ObjectCreator(String path) {
        this.file = new File(path);
    }

    public void newObjectFile() throws Exception {
        if (object == null) {
            throw new Exception("Do setObject(Object) first!");
        } else {
            try {
                if (file.isFile()) {
                    file.delete();
                }
                FileOutputStream FOS = new FileOutputStream(this.file);
                ObjectOutputStream OOS = new ObjectOutputStream(FOS);

                // Write objects to file
                OOS.writeObject(object);
                OOS.close();
                FOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public T getObjectFile() {
        try {
            FileInputStream FIS = new FileInputStream(this.file);
            ObjectInputStream OIS = new ObjectInputStream(FIS);

            // Read objects
            T object = (T) OIS.readObject();
            OIS.close();
            FIS.close();

            return object;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return (T) new Object();
    }

    public boolean doesFileExist() {
        return file.isFile();
    }
}
