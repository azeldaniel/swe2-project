package swe2slayers.gpacalculationapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import swe2slayers.gpacalculationapplication.models.Year;
//ToDO: fix comments add more details
//where actual saving of gpa and other data affecting gpa will be stored in local storage
//this class handles how the data will be save and the db schema design
public class GpaAdapter {
    private Year year;

    static final String dbName="GpaData.db";
    //a db version is needed for arguments, we expect to use the same scheme right through(I hope)
    static final int ver=1;
    //In case a password is decided to be set
    public static final int nameCol = 1;
    //if table insert worked
    public boolean status=false;

    //String holding sql create statements for creation in SQLDataBaseHelper
    static final String createGPA="create table gpa(GpaID integer primary key autoincrement,GpaDegree real,GpaCumulative real);";

    static final String CreateYear="create table year( YearID text primary key,YearDesc  text);";
    static final String CreateSemester="create table semester( SemesterID text primary key,YearID text, SemesterDesc text,Grade text, FOREIGN KEY(YearID) REFERENCES year(YearID));";
    static final String CreateCourse="create table course( CourseID text primary key,SemesterID text, CourseName text, CourseCode text,Credits real, FOREIGN KEY(SemesterID) REFERENCES semester(SemesterID));";
    static final String CreateAssess="create table assessment( AssessmentID text primary key,CourseID text, AssessmentType text, Grade text,Total integer, Over integer,weight, FOREIGN KEY(CourseID) REFERENCES course(CourseID));";


    //create sqlite db instance
    public static SQLiteDatabase sqlDB;
    private final Context context;
    private static SQLDataBaseHelper sqldbHelper;

    //create context
    public GpaAdapter(Context context){
        this.context=context;
        sqldbHelper=new SQLDataBaseHelper(context,dbName,null,ver);
    }

    //like file reading. we need to access/open the db to add,update etc
    public GpaAdapter accessDB() throws SQLException{
        sqlDB=sqldbHelper.getWritableDatabase();
        return this;
    }

    //returns the database
    public SQLiteDatabase getDB(){
        return sqlDB;
    }

//GPA stands alone as user can choose to either edit directly or get the values by using data from other tables calculations

    //<editor-fold desc="GPA">

    //insert data into Gpa table
    public  boolean insertGpa(String gpaID, double gpaDegree,double gpaCumulative){
        try {
            ContentValues cv=new ContentValues();
            cv.put("GpaID",gpaID);
            cv.put("GpaDegree",gpaDegree);
            cv.put("GpaCumulative",gpaCumulative);
            sqlDB=sqldbHelper.getWritableDatabase();
            sqlDB.insert("gpa",null,cv);
            Log.i("GPA Table", "insertGPAData: trying now... ");
        }catch (Exception DBinputFailed){
            Log.e("GPA Table", "insertGPA: Failed");
            status =false;
        }
        Log.i("GPA Table", "insertGPA: Successful");
        status=true;
        return status;
    }

    //deletes GPA by ID
    public int deleteEntryGpa(String gpaID){
        String where="GpaID=?";
        int numRec=sqlDB.delete("gpa",where,new String[]{gpaID});
        return numRec;
    }

    //get data using GpaID
    @Nullable
    public HashMap getGpaDetails(String gpaID){
        HashMap gpaMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("gpa",null,"GpaID=?",new String[]{gpaID},null,null,null);
        if(cur.getCount()<1)
            return null;
        cur.moveToFirst();
        String gpaDegree=cur.getString(cur.getColumnIndex("GpaDegree"));
        String gpaCumulative=cur.getString(cur.getColumnIndex("GpaCumulative"));
        gpaMap.put("gI",gpaID);
        gpaMap.put("gD",gpaDegree);
        gpaMap.put("gC",gpaCumulative);
        return gpaMap;
    }

    //update fields in Gpa using GPAID
    public void updateGpa(String gpaID, double gpaDegree,double gpaCumulative){
        ContentValues cv=new ContentValues();
        cv.put("GpaID",gpaID);
        cv.put("GpaDegree",gpaDegree);
        cv.put("GpaCumulative",gpaCumulative);

        String where="GpaID=?";
        sqlDB.update("gpa",cv,where,new String[]{gpaID});

    }
    //</editor-fold>


    //<editor-fold desc="Year Table">
    public  boolean insertYear(String yearId, String yearDesc){
        try {
            ContentValues cv=new ContentValues();
            cv.put("YearID",yearId);
            cv.put("YearDesc",yearDesc);
            sqlDB=sqldbHelper.getWritableDatabase();
            sqlDB.insert("year",null,cv);
            Log.i("Year Table", "insertYearData: trying now... ");
        }catch (Exception DBinputFailed){
            Log.e("Year Table", "insertYear: Failed");
            status =false;
        }
        Log.i("Year Table", "insertYear: Successful");
        status=true;
        return status;
    }

    //deletes Year by ID
    public int deleteEntryYear(String yearID){
        String where="YearID=?";
        int numRec=sqlDB.delete("year",where,new String[]{yearID});
        return numRec;
    }

    //
    @Nullable
    public HashMap getYearDetails(String yearID){
        HashMap yearMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("year",null,"YearID=?",new String[]{yearID},null,null,null);
        if(cur.getCount()<1)
            return null;
        cur.moveToFirst();
        String yearDesc=cur.getString(cur.getColumnIndex("YearDesc"));
        yearMap.put("yI",yearID);
        yearMap.put("yD",yearDesc);
        return yearMap;
    }

    public void updateYear(String yearID,String yearDesc){
        ContentValues cv=new ContentValues();
        cv.put("YearID",yearID);
        cv.put("YearDesc",yearDesc);

        String where="YearID=?";
        sqlDB.update("year",cv,where,new String[]{yearID});

    }

    //</editor-fold>

    //<editor-fold desc="Semester Table">
    public  boolean insertSemester(String semesterID,String yearId, String semesterDesc,String grade){
        try {
            ContentValues cv=new ContentValues();
            cv.put("SemesterID",semesterID);
            cv.put("YearID",yearId);
            cv.put("SemesterDesc",semesterDesc);
            cv.put("Grade",grade);
            sqlDB=sqldbHelper.getWritableDatabase();
            sqlDB.insert("semester",null,cv);
            Log.i("Semester Table", "insertSemesterData: trying now... ");
        }catch (Exception DBinputFailed){
            Log.e("Semester Table", "insertSemester: Failed");
            status =false;
        }
        Log.i("Semester Table", "insertSemester: Successful");
        status=true;
        return status;
    }

    //deletes Semester by ID
    public int deleteEntrySemester(String semesterID){
        String where="SemesterID=?";
        int numRec=sqlDB.delete("semester",where,new String[]{semesterID});
        return numRec;
    }

    //
    @Nullable
    public HashMap getSemesterDetails(String semesterID){
        HashMap semesterMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("semester",null,"SemesterID=?",new String[]{semesterID},null,null,null);
        if(cur.getCount()<1)
            return null;
        cur.moveToFirst();
        String semesterDesc=cur.getString(cur.getColumnIndex("SemesterDesc"));
        String yearId=cur.getString(cur.getColumnIndex("YearID"));
        String grade=cur.getString(cur.getColumnIndex("Grade"));
        semesterMap.put("sI",semesterID);
        semesterMap.put("yI",yearId);
        semesterMap.put("sD",semesterDesc);
        semesterMap.put("g",grade);
        return semesterMap;
    }

    public void updateSemester(String semesterID,String yearID,String semesterDesc,String grade){
        ContentValues cv=new ContentValues();
        cv.put("SemesterID",semesterID);
        cv.put("YearID",yearID);
        cv.put("SemesterDesc",semesterDesc);
        cv.put("Grade",grade);
        String where="SemesterID=?";
        sqlDB.update("semester",cv,where,new String[]{semesterID});

    }
    //</editor-fold>

    //<editor-fold desc="Course Table">
    public  boolean insertCourse(String courseID,String semesterID,String courseName,String courseCode, double credits){
        try {
            ContentValues cv=new ContentValues();
            cv.put("SemesterID",semesterID);
            cv.put("CourseID",courseID);
            cv.put("CourseName",courseName);
            cv.put("CourseCode",courseCode);
            cv.put("Credits",credits);

            sqlDB=sqldbHelper.getWritableDatabase();
            sqlDB.insert("course",null,cv);
            Log.i("Course Table", "insertCourseData: trying now... ");
        }catch (Exception DBinputFailed){
            Log.e("Course Table", "insertCourse: Failed");
            status =false;
        }
        Log.i("Course Table", "insertCourse: Successful");
        status=true;
        return status;
    }

    //deletes Course by ID
    public int deleteEntryCourse(String courseID){
        String where="CourseID=?";
        int numRec=sqlDB.delete("course",where,new String[]{courseID});
        return numRec;
    }

    //
    @Nullable
    public HashMap getCourseDetails(String courseID){
        HashMap courseMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("course",null,"CourseID=?",new String[]{courseID},null,null,null);
        if(cur.getCount()<1)
            return null;
        cur.moveToFirst();
        String semesterId=cur.getString(cur.getColumnIndex("SemesterID"));
        String cName=cur.getString(cur.getColumnIndex("CourseName"));
        String cCode=cur.getString(cur.getColumnIndex("CourseCode"));
        String cedits=cur.getString(cur.getColumnIndex("Credits"));
        courseMap.put("cI",courseID);
        courseMap.put("sI",semesterId);
        courseMap.put("cN",cName);
        courseMap.put("cC",cCode);
        courseMap.put("c",cedits);
        return courseMap;
    }

    public void updateCourse(String courseID,String semesterID,String courseName,String courseCode,double credits){
        ContentValues cv=new ContentValues();
        cv.put("SemesterID",semesterID);
        cv.put("CourseID",courseCode);
        cv.put("CourseName",courseName);
        cv.put("CourseCode",courseCode);
        cv.put("Credits",credits);
        String where="CourseID=?";
        sqlDB.update("course",cv,where,new String[]{courseID});

    }
    //</editor-fold>

    //<editor-fold desc="Assessment">
    public  boolean insertAssessment(String assessmentID,String courseID,String assessmentType, String grade, int total,int over,double weight){
        try {
            ContentValues cv=new ContentValues();
            cv.put("AssessmentID",assessmentID);
            cv.put("CourseID",courseID);
            cv.put("AssessmentType",assessmentType);
            cv.put("Grade",grade);
            cv.put("Total",total);
            cv.put("Over",over);
            cv.put("Weight",weight);

            sqlDB=sqldbHelper.getWritableDatabase();
            sqlDB.insert("assessment",null,cv);
            Log.i("Assessment Table", "insertAssessmentData: trying now... ");
        }catch (Exception DBinputFailed){
            Log.e("Assessment Table", "insertAssessment: Failed");
            status =false;
        }
        Log.i("Assessment Table", "insertAssessment: Successful");
        status=true;
        return status;
    }

    //deletes Assessment by ID
    public int deleteEntryAssessment(String assessmentID){
        String where="AssessmentID=?";
        int numRec=sqlDB.delete("assessment",where,new String[]{assessmentID});
        return numRec;
    }

    //
    @Nullable
    public HashMap getAssessmentDetails(String assessmentID){
        HashMap AssessMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("assessment",null,"AssessmentID=?",new String[]{assessmentID},null,null,null);
        if(cur.getCount()<1)
            return null;
        cur.moveToFirst();
        String courseID=cur.getString(cur.getColumnIndex("CourseID"));
        String aType=cur.getString(cur.getColumnIndex("AssessmentType"));
        String grade=cur.getString(cur.getColumnIndex("Grade"));
        String total=cur.getString(cur.getColumnIndex("Total"));
        String over=cur.getString(cur.getColumnIndex("Over"));
        String weight=cur.getString(cur.getColumnIndex("Weight"));
        AssessMap.put("cI",courseID);
        AssessMap.put("aI",assessmentID);
        AssessMap.put("aT",aType);
        AssessMap.put("g",grade);
        AssessMap.put("t",total);
        AssessMap.put("o",over);
        AssessMap.put("w",weight);
        return AssessMap;
    }

    public void updateAssessment(String assessmentID,String courseID,String assessmentType, String grade, int total,int over,double weight){
        ContentValues cv=new ContentValues();
        cv.put("AssessmentID",assessmentID);
        cv.put("CourseID",courseID);
        cv.put("AssessmentType",assessmentID);
        cv.put("Grade",grade);
        cv.put("Total",total);
        cv.put("Over",over);
        cv.put("Weight",weight);
        String where="AssessmentID=?";
        sqlDB.update("assessment",cv,where,new String[]{assessmentID});

    }
    //</editor-fold>


//Queries to pull the specifics

    //get YearDesc (by yearID)
    public String getYearDesc(String yearID){
        sqlDB=sqldbHelper.getReadableDatabase();
        Cursor cur=sqlDB.query("year",null,"YearID=?",new String[]{yearID},null,null,null);
        if(cur.getCount()<1)
            return null;
        if(!cur.moveToFirst()) Log.e("Year Description", "getYearDesc: Failed");;
        String yearDesc=cur.getString(cur.getColumnIndex("YearDesc"));

        return yearDesc;
    }

    //get Semester data data(YearID and semesterID);
    //might not need yearID ToDO: review
    public HashMap SemesterDetails (String yearID,String semesterID){
        HashMap semesterMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();
        //SQL says get from table semester, from columns SesterDesc and Grade, where YearId and SemesterID, using IDs given in function argument, null,null,null
        Cursor cur=sqlDB.query("semester",new String[]{"SemesterDesc","Grade"},"YearID=?"+"and SemesterID=?",new String[]{yearID,semesterID},null,null,null);

        //if query returns nothing the cursor cant move to first result thus will return false
        if(!cur.moveToFirst()) Log.e("Semester dets", "Semester: Failed");
        //get table data from query by pulling it from cursor
        String semesterdesc=cur.getString(cur.getColumnIndexOrThrow("SemesterDesc"));
        String semestergrade=cur.getString(cur.getColumnIndexOrThrow("Grade"));

        //put in Hashmap
        semesterMap.put("sD",semesterdesc);
        semesterMap.put("g",semestergrade);

        //return Hashmap
        return semesterMap;
    }
    //get course data (by semesterID) TODO: Finish up
    public HashMap courseDetails(String semesterID,String CourseID){
        HashMap courseMap=new HashMap();
        sqlDB=sqldbHelper.getReadableDatabase();

        return courseMap;
    }
    //get Assessment data (by courseID) TODO:ToDO:ToDOTODO:TODOTODO:TODODODODO


}
