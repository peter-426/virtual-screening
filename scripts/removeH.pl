$#ARGV == 0 || die "Usage: $0 [H|I|R] \n";
 
$STAT = $ARGV[0];

`condor_q | grep " $STAT " | grep perl > temp.txt`;


open(FD, "temp.txt") || die "$!";


while( <FD> )
{
  @a = split;

  print "condor_rm $a[0] \n";

  `condor_rm $a[0]`;
}

