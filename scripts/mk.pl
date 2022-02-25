
$#ARGV == 3 || die "Usage: $0  GN start size maxStart";

$GN    = $ARGV[0];
$START = $ARGV[1];
$SIZE  = $ARGV[2];
$MAX_START   = $ARGV[3];


for($i = $START; $i <= $MAX_START; $i += $SIZE)
{
open(FD, ">d_${GN}_${i}.classad") || die "$!";


$end = $i + $SIZE - 1;

print "\n making ads for GN=$GN $i to $end \n\n"; 

print FD "
universe     = vanilla
Executable   = /usr/bin/perl
Log          = perl.log

RESERVED_SWAP = 0
      
Error   = err.\$(Process)
Input   = dock_${GN}_${i}_${end}.pl
Output  = out.\$(Process)
Log     = foo.log


Queue";

close FD;


open(FD, ">dock_${GN}_${i}_${end}.pl") || die "$!";

print FD "use dock;

\$GN = \"$GN\";

\$start = \"$i\";
\$end   = \"$end\";

runDock();\n";

close FD;
 
}
