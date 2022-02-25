
# verify.pl 
# checks whether or not every out-GN file has a valid log file and that every log file has an out-GN file

use Cwd;

$#ARGV  == 1 || die "Usage $0 docking-set-ID  GN ";

$dr        =  $ARGV[0];
$GN        =  $ARGV[1];

@dirs = glob "$dr*";

$filename = "$GN-ZINC-IDS$threshold.txt";

@scores=();


$total = 0;
$count = 0;

parseLog();
parseOut();

sub parseLog
{
  foreach $d (@dirs)
  {
	$logFolder      =  "$d/log-$GN";

	@files = glob "*$logFolder/*";    

	foreach $file (@files) {
	   
	   if ($file =~ /ZINC.*log.txt$/) 
	   {
	        open(FD, "$file") || die "$!";

                $valid = 0;
		  while (<FD>) 
                  {
			if (/^   1 .*/) 
                     {
                       $valid = 1; 
			  #print "$file $_"; 
			  $record = "$file $_";
			  @fields = split(" ", $record);
	
			  last;
			}
		  }		  
		  close FD;

                  if (! $valid ) 
                  { 
                    if (-e $file) { print "removing $file\n"; unlink($file);}
                    $file =~ s/log-$GN/out-$GN/;
                    $file =~ s/ZINC/out_ZINC/;
                    $file =~ s/_log.txt//;
                    if (-e $file) { print "removing $file\n"; unlink($file);}
                  }
		}
	 }  
  }
}

sub parseOut
{
  foreach $d (@dirs)
  {
	$outFolder      =  "$d/out-$GN";
	$logFolder      =  "$d/log-$GN";

	@files = glob "*$logFolder/*";   

        %ht = (); 

	 foreach $file (@files) 
        {
	     if ( $file =~ /ZINC.*pdbqt_log.txt$/ ) 
	     {    
                  $file =~ s/$d\/log-$GN\///;

                  $file =~ s/_log.txt//;
                  $ht{$file} = $file;
	     }
	 }  
        
	@files = glob "*$outFolder/*";  

	 foreach $file (@files) 
        {
	   
	     if ($file =~ /out_ZINC.*pdbqt$/) 
	     {
                  $temp = $file;
                  $temp =~ s/$d\/out-$GN\///;
                  $temp =~ s/out_//;
                  if ( ! $ht{$temp} ) {  print "removing $temp\n"; unlink($file); }
	     }
	 }  

  }
}

