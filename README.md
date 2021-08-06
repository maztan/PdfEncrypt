ncryption Software Requirements:
This offline desktop software should be based on Java and open source api’s. This should work
on WINDOWS 7 desktops and above. Below are preferred ones, but if you have any other
suggestions, please do let me know.
1. Let me know the Java version you will be using.
2. For PDF password protection/unprotection, please look into OpenPDF or ICEPDF. I
   think this can be used for page count as well. Kindly suggest
3. For zipping/unzipping with pw - https://github.com/srikanth-lingala/zip4j
   Final Deliverables:
   Source code as well as installer.
1) Login Screen:
   Only users with admin and super admin roles should be able to login.
   In addition to the online database for username and passwords, need static username and
   password also.
   ● Password
   ● Submit Button: The users credentials and roles are validated and if correct they are logged in.
   ● Company Logo
   ● Copyright message
2) PDF Encryption/Decryption Page:
   This page will have the below fields.
   ● User password - This password (pw) will allow user only to open and view PDF. They
   should be able to copy text and do ocr on pw protected pdfs.
   ● Owner password - This will allow users also to edit PDF.
   ● Source pdf location - They can directly paste the folder location or browse and select it.
   This folder may or may not have subfolders. If there are subfolders, you will do the
   process within the first level of subfolders.
   ● Destination pdf location - You will create a folder called Z in the source PDF location and
   within that you will create again new subfolders with names similar to source pdf
   location.
   Buttons:
1. Encrypt button
   a. Pw protect with both user and owner pw all the pdf’s within source pdf location.
   b. After pw protecting paste the pw protected file in corresponding named
   subfolder within Z.
   c. If a PDF throws error when pw protecting, don’t pw protect that file, copy and
   paste the file without pw protecting in destination folder in z.
   d. If a subfolder file has non-pdf files, copy and paste them in corresponding folder
   within Z.
   e. Override any errors and complete the process, and type the errors in a log
   notepad in Source PDF location.
   f. Show progress bar or something similar.
2. Decrypt button
   a. Remove Pw protection with both user and owner pw all the pdf’s within source
   pdf location.
   b. After removing pw protecting paste the unprotected file in corresponding folder
   within Z.
   c. If a subfolder file has non-pdf files, copy and paste them in corresponding folder
   within Z.
   d. Show progress bar or something similar.
3. Page count. You will find total number of pages in each pdf and total number of pages in
   all the pdf.
   a. First it should create Total_Input.txt in Source pdf location.
   ■ In this text pad, at the top, you would type number of subfolders and
   total number of pdf pages as “There are __________ subfolders and
   Total Pages in all subfolders: ____________”.
   ■ Below this you will type each “Subfolder name” TAB “Number of files in
   that subfolder” TAB “Total number PDF pages in that subfolder”.
   ■ Below that you will type “PDF Name” TAB “_______ pages” (you have to
   fill page number in the blank)
   ■ Repeat the process for all the subfolders and then save and close the
   notepad.
   ■ Eg:
   There are 5 subfolders and Total Pages in all subfolders: 12553
   KMCO Plant Explosion Ms. Joe Martinez_SUPPLEMENT 26 files
   888 pages
   KMCO Plant Explosion Ms. Jane
   Martinez_SUPPLEMENT_04022019_12031986 26 files
   888 pages
1. Deyanira Martinez 000125 - 000191.pdf 67 pages
2. R000635 - R000646 D. Martinez_Advanced Diagnostics DBA
   Chopra Imaging_Billing.pdf 12 pages
3. R000647 - R000658 D. Martinez_Advanced Diagnostics DBA
   Chopra Imaging_Medical.pdf 12 pages
4. R000659 - R000663 D. Martinez_Advanced Diagnostics Hospital
   East_Billing.pdf 5 pages
   b. Within each subfolder, create a notepad named
   “PageCount_Subfoldername.txt”
   ■ At the top of this notepad type “Subfolder name” TAB “Number of files
   in that subfolder” TAB “Total number PDF pages in that subfolder”.
   ■ Below that type the PDF file name within that subfolder and the page
   number of each pdf and total page number within each subfolder.
   ■ Below that you will type “PDF Name” TAB “_______ pages” for all the
   PDFs
   c. If you are unable to find PAGE number for any PDF, just type the PDF name
   followed by TAB followed by “ERROR”, but don’t stop the process. Complete the
   process.
   d. Show progress bar or something similar.


Server=198.46.81.195
Database=recsnd5_Pr0duct10napplicat1on
Uid=recsnd5_teamapp
Pwd=g@ndh!555j!