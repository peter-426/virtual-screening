

$#ARGV == 0 || die "Usage: $0 filename \n"; 

$filename = $ARGV[0];

$filenameNPD = "npd_purch.xls";

open(FD1, "$filename") || die "Failed to open $filename \n";

@lines = <FD1>;

close FD1;

open(FD2, "$filenameNPD" ) || die "Failed to open file";
$headings = <FD2>;

$heading =~ s/_/\\\_/g;
print "Score\t$headings";
close FD2;


foreach $line (@lines)
{
  ($code, $score) = split(/ /,$line);
  #chomp $score;  

  #print "$code\n";

  open(FD2, "$filenameNPD" ) || die "Failed to open file";

  $matched = 0;
  while (<FD2>)
  {
    if ( $_ =~ /.*$code.*/ ) 
    {    
       chomp;
       print "$score\t$_ \n";
       $matched = 1;
       #last;
    }
  }
  close FD2;

  if ($matched == 0) { print "$score \t $code   \t    \t   \t  \t  \t  \t  \n"; }

  
}



