 use Cwd;

$#ARGV == 0 || die "Usage: $0 docking-ID\n";

$dr = "$ARGV[0]";
# $GN = "$ARGV[1]"; 
print "\n";

@dirs = glob "$dr*";

foreach $d (@dirs) 
{
  chdir $d;

  print "$d \n";

  @files = glob "*.classad";

  foreach $f (@files)
  {
    # if ($f =~ /$GN/ ) {
      `condor_submit $f`; 
       print "$f \n";
    # }
  }
  chdir "..";
}

