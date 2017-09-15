![QRScan](qrscan.png)
# QRScan: recognition of QR codes in PDF files of scanned documents

![QRScan screenshot main screen](qrscan_capture.png)

If you have a large collection of PDF files of scanned documents, QRScan can help you keep track of your documents and set up basic file archiving. QRScan recognizes QR codes in PDF files of scanned documents and then reports these QR codes in a CSV file. We used the QRScan tool to send questionnaires labeled with individual QR codes. For the returned questionnaires, we used QRScan to register and to archive the scanned responses.

The QR codes can be stored as a file attribute (for faster future reference). If automatic QR code recognition fails, you can manually add the file attribute to the PDF file. QRScan can move and rename the PDF files according to their QR code (with sequential numbering appended as a suffix if PDF files share the same QR code). If you provide a text file with codes, QRScan can generate the PNG image files with QR codes for each line, which you can use to incorporate QR codes in your printed documents in the first place.

The provided executable JAR is ready to use. Good QR code recognition is attained when documents have been scanned at a resolution of 300 DPI. Java 8 or more recent is required. A big thanks to the following projects: PDFBox by The Apache Software Foundation, Java ImageIO plugin for JBIG2, Java Advanced Imaging Image I/O Tools API, and the ZXing project. 

# How to install

You need to have Java installed. Next, download and execute the latest release (i.e. the *jar* file). On Windows, simply double-click the *jar* file to execute.
