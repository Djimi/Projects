package tree.directory.models;

/**
 * Created by Damyan Manev on 18-Jan-16.
 */
public class LineInfo implements Comparable<LineInfo> {

    private int lineNumber;
    private String line;
    private String fileName;

    public LineInfo(int lineNumber, String line, String fileName) {
        this.lineNumber = lineNumber;
        this.line = line;
        this.fileName = fileName;

    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineInfo lineInfo = (LineInfo) o;

        if (lineNumber != lineInfo.lineNumber) return false;
        if (line != null ? !line.equals(lineInfo.line) : lineInfo.line != null) return false;
        return fileName != null ? fileName.equals(lineInfo.fileName) : lineInfo.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = lineNumber;
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(LineInfo o) {
        int compareResult = this.fileName.compareTo(o.getFileName());
        if (compareResult > 0) {
            return 1;
        } else if (compareResult < 0) {
            return -1;
        } else { //compareResult == 0
            return this.lineNumber - o.getLineNumber();
        }
    }
}
