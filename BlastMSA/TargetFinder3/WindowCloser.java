package target;

// WindowCloser.java


import java.awt.event.*;


public class WindowCloser extends WindowAdapter
{
    @Override
  public void windowClosing ( WindowEvent e )
  {
		//Parse.saveNotes();
    windowExit();
  }

  public void windowExit()
  {
		//Parse.saveNotes();
    System.exit( 0 );
  }
}




