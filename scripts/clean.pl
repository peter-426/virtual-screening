use Cwd;
 
$#ARGV == 0 || die "Usage: $0 docking-ID \n";

$dr        = "$ARGV[0]";

print "\n";

@dirs = glob "$dr*";

foreach $d (@dirs) 
{
  chdir $d;

  print "$d \n";

  @files_ad = glob "*.classad";
  @files_pl = glob "*.pl";


  foreach $f (@files_ad)
  {
  `/bin/rm $f`;
  }

  foreach $f (@files_pl)
  {
  `/bin/rm $f`;
  }

  chdir "..";
}

