
# report.pl 
# generates a  report of top scoring ligands sorted by score

use Cwd;

$#ARGV  == 3 || die "Usage $0 docking-set-ID  GN score-threshold mode[1|-1]";

$dr        =  $ARGV[0];
$GN        =  $ARGV[1];
$threshold =  $ARGV[2];
$mode      =  $ARGV[3];

@dirs = glob "$dr*";

$filename = "$GN-ZINC-IDS$threshold.txt";

@scores=();


$total = 0;
$count = 0;

parse();
showSorted();
save();

sub parse
{

  $min =  1000;
  $max = -1000;

  foreach $d (@dirs)
  {
	#$ligandFolder   = "$d/ligand";
	#$receptorFolder = "$d/receptor-$GN";
	#$outFolder      = "$d/out-$GN";
	$logFolder      =  "$d/log-$GN";

	#print "\n****************************\n*  $d  \n****************************\n";
	#print "ligandFolder   = $ligandFolder \n";
	#print "receptorFolder = $receptorFolder \n";
	#print "outFolder      = $outFolder \n";
	#print "logFolder      = $logFolder \n\n";

	#chdir($logFolder) or die "Error: failed to chdir to $path $!";

	#opendir(DIR, $logFolder);
	#readdir(DIR);
	#closedir(DIR);

	@files = glob "*$logFolder/*";    

	foreach $file (@files) {
	   
	   if ($file =~ /ZINC.*log.txt$/) 
	   {
		  open(FD, "$file") || die "$!";

             
		  while (<FD>) 
                  {
			if (/^   1 .*/) {

                   
			    #print "$file $_"; 
			    $record = "$file $_";
			    @fields = split(" ", $record);

                         $score = $fields[2];
	
                         $count += 1;
                         $total += $score;

                         if ($min > $score )  {  $min = $score; }
                         if ($max < $score )  {  $max = $score; }
                          
	
                         if ($mode == 1 && $score <= $threshold ) { 
                            push(@scores, $record);  
                            #print "$record\n";
                          }
                         elsif ($mode == -1 && $score >= $threshold ) {
                            push(@scores, $record);
                            #print "$record\n";
                          }
			  last;
			}
		  }		  
		  close FD;
		}
	 }  
  }
}

sub showSorted
{
	#foreach $s (@scores) { print "$s"; }
		
	@pre = map { [$_, split " " ] } @scores;
	@post = sort custom @pre;
	@sorted = map { $_->[0] } @post;

	print "\n";
	print "$_" for @sorted;

        if($count > 0) { $avg = $total/$count; }
        printf "\n === average of %d scores = %2.2f,  min = %2.2f,  max = %2.2f \n", $count, $avg, $min, $max;
}


sub save
{
       print "----------\n";
	open(FDZ, ">$filename") || die "open $filename failed.";
       @fields = ();
	foreach $line (@sorted) 
	{ 
  		#print "$line \n";
        	@fields = split(/ +/, "$line");
  		$fields[0] =~ s/.*$GN\///;
  		$fields[0] =~ s/\.pdbqt_log.txt.*//;
  		print "$fields[0] $fields[2] \n";
        	print FDZ "$fields[0] $fields[2] \n";
	}
	close FDZ;
}

sub custom 
{
	$a->[3] <=> $b->[3];
}

