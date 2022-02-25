use Cwd;
 
$#ARGV == 0 || die "Usage: $0 docking-ID\n";

$dr = "$ARGV[0]";
# $GN = "$ARGV[1]"; 
print "\n";

@dirs = glob "$dr*";

foreach $d (@dirs) 
{
  chdir $d;

  #print "$d \n";

  @files = glob "*.pl";

  foreach $f (@files)
  {
    # if ($f =~ /$GN/ ) {
       print "nohup /usr/bin/perl $d/$f & \n"; 
       #print "$f \n";
    # }
  }
  chdir "..";
}

