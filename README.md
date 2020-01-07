![QRScan](qrscan.png)
# QRScan: recognition of QR codes in PDF files of scanned documents

![QRScan screenshot main screen](qrscan_capture.png)

If you have a large collection of PDF files of scanned documents, QRScan can help you keep track of your documents and set up basic file archiving. QRScan recognizes QR codes in PDF files of scanned documents and then reports these QR codes in a CSV file. We used the QRScan tool to send questionnaires labeled with individual QR codes. For the returned questionnaires, we used QRScan to register and to archive the scanned responses.

The QR codes can be stored as a file attribute (for faster future reference). If automatic QR code recognition fails, you can manually add the file attribute to the PDF file. QRScan can move and rename the PDF files according to their QR code (with sequential numbering appended as a suffix if PDF files share the same QR code). If you provide a text file with codes, QRScan can generate the PNG image files with QR codes for each line, which you can use to incorporate QR codes in your printed documents in the first place.

The provided executable JAR is ready to use, but made with Java =< 8. The code has been updated to Java 13, but because dependencies are not yet using "info-modular" there is not yet an updated JAR.
 
Good QR code recognition is attained when documents have been scanned at a resolution of 300 DPI. [Java 8](https://www.java.com/en/download/) or more recent is required. A big thanks to the following projects: [PDFBox by The Apache Software Foundation](https://pdfbox.apache.org/), [Java ImageIO plugin for JBIG2](https://github.com/levigo/jbig2-imageio), [Java Advanced Imaging Image I/O Tools API](https://github.com/jai-imageio/jai-imageio-jpeg2000), and the [ZXing project](https://github.com/zxing).

# How to install

You need to have Java installed. Next, download and execute the latest release (i.e. the *jar* file). On Windows, simply double-click the *jar* file to execute.
