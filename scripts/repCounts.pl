 
$#ARGV == 1 || die "Usage: $0 docking-ID GN\n";

$dr        = "$ARGV[0]";
$GN        = "$ARGV[1]";

print "\n";

@dirs = glob "$dr*";

foreach $d (@dirs) 
{
  $n=`ls -lt $d/out-$GN | grep pdbqt | wc -l`;
  printf "  %30.30s ==> %10.10s\n", $d, $n;
}

