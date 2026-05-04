package curvemorph;

import java.awt.AWTEvent;
import java.awt.Checkbox;
import java.awt.Choice;

import ij.Prefs;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;

public class CMDialog implements DialogListener
{
	
	/**  0 - morphing; 1 - xy interpolation**/
	public int nAlgorithm = (int)Prefs.get( "CurveMorph.nAlgorithm", 0 );
	
	/** in case of morph algorithm, 0 - centroid, 1 - closest end point, 2 - start always first, 3 - end always first **/
	public int nMorphType = (int)Prefs.get( "CurveMorph.nMorphType", 0 );
	
	/** range of morphing 0 - image defined, 1 - ROIs defined**/
	public int nRange = (int)Prefs.get( "CurveMorph.nRange", 0 );
	
	/** whether to add to overlay **/
	public boolean bAddToOverlay = Prefs.get( "CurveMorph.bAddToOverlay", true );
	
	/** whether to add to ROI Manager **/
	public boolean bAddToManager = Prefs.get( "CurveMorph.bAddToManager", false );
	
	/** whether to make kymograph **/
	public boolean bMakeKymograph = Prefs.get( "CurveMorph.bMakeKymograph", false );

	/** kymograph transverse type 0 - MAX proj, 1 - Average Proj **/
	public int nKymoType = (int)Prefs.get( "CurveMorph.nKymoType", 0 );
	
	/** kymograph alignment type 0 - center, 1 - start, 2 - end **/
	public int nKymoAlign = (int)Prefs.get( "CurveMorph.nKymoAlign", 0 );
	
	Choice chAlgo;
	Choice chMorphMethod;
	Checkbox cMakeKymograph;
	Choice chKymoType;
	Choice chKymoAlign;
	
	GenericDialog gdParams;
		
	boolean showDialog()
	{
		gdParams = new GenericDialog( "CurveMorph parameters" );
		final String [] sAlgorithm = new String[] {"Shape morphing", "XY interpolation"};
		final String [] sMorphType = new String[] {"Centroid", "Closest end point", "First ends", "Last ends"};
		final String [] sRange = new String[] {"All image span", "Defined by ROIs"};
		final String [] sKymoType = new String[] {"Maximum", "Average"};
		final String [] sKymoAlign = new String[] {"Center", "Start point", "End point"};
		gdParams.addChoice( "Algorithm", sAlgorithm,  null);
		chAlgo = ((Choice)gdParams.getChoices().get( 0 ));
		chAlgo.select( nAlgorithm );
		gdParams.addChoice( "Morph type", sMorphType,  null);
		chMorphMethod = ((Choice)gdParams.getChoices().get( 1 ));
		chMorphMethod.select( nMorphType );
		gdParams.addChoice( "Range", sRange,  null);
		((Choice)gdParams.getChoices().get( 2 )).select( nRange );
		
		gdParams.addCheckbox( "Add to overlay", bAddToOverlay );
		gdParams.addCheckbox( "Add to ROI Manager", bAddToManager );
		gdParams.addCheckbox( "Make kymograph", bMakeKymograph );
		cMakeKymograph = ( Checkbox ) gdParams.getCheckboxes().get( 2 );
		gdParams.addChoice( "Kymo transverse intensity", sKymoType,  null);
		chKymoType = ((Choice)gdParams.getChoices().get( 3 ));
		chKymoType.select( nKymoType );
		gdParams.addChoice( "Kymo align", sKymoAlign,  null);
		chKymoAlign = ((Choice)gdParams.getChoices().get( 4 ));
		chKymoAlign.select( nKymoAlign );
		
		gdParams.addDialogListener( this );
		updateDialog();
		gdParams.pack();
		gdParams.showDialog();
		
		if ( gdParams.wasCanceled() )
			return false;
		return true;
	}
	
	void updateDialog()
	{
		if(chAlgo.getSelectedIndex() == 0)
		{
			chMorphMethod.setEnabled( true );
		}
		else
		{
			chMorphMethod.setEnabled( false );
		}
		if(cMakeKymograph.getState())
		{
			chKymoType.setEnabled( true );
			chKymoAlign.setEnabled( true );
		}
		else
		{
			chKymoType.setEnabled( false );
			chKymoAlign.setEnabled( false );
		}
	}
	
	@Override
	public boolean dialogItemChanged( GenericDialog gd, AWTEvent e )
	{
		if(e != null)
		{
			updateDialog();
		}
		if(gdParams.wasOKed())
		{
			readDialogParameters();
		}
		return true;
	}
	
	void readDialogParameters()
	{
		nAlgorithm = gdParams.getNextChoiceIndex();
		Prefs.set( "CurveMorph.nAlgorithm", (double) nAlgorithm );
		
		if(nAlgorithm == 0)
		{
			nMorphType = gdParams.getNextChoiceIndex();
			Prefs.set( "CurveMorph.nMorphType", (double) nMorphType );
		}
		else
		{
			gdParams.getNextChoiceIndex();
		}
		
		
		nRange = gdParams.getNextChoiceIndex();
		Prefs.set( "CurveMorph.nRange", (double) nRange );
		
		bAddToOverlay = gdParams.getNextBoolean();
		Prefs.get( "CurveMorph.bAddToOverlay", bAddToOverlay );
		
		bAddToManager = gdParams.getNextBoolean();
		Prefs.get( "CurveMorph.bAddToManager", bAddToManager );
		
		bMakeKymograph = gdParams.getNextBoolean();
		Prefs.get( "CurveMorph.bMakeKymograph", bMakeKymograph );
		
		if(bMakeKymograph)
		{
			nKymoType = gdParams.getNextChoiceIndex();
			Prefs.set( "CurveMorph.nKymoType", (double) nKymoType );
			nKymoAlign = gdParams.getNextChoiceIndex();
			Prefs.set( "CurveMorph.nKymoAlign", (double) nKymoAlign );
		}
	}
	
}
