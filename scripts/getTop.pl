use File::Copy;
 

$#ARGV == 3 || die "Usage: $0 docking-ID GN topLigandsFile score\n";

$dr              = "$ARGV[0]";
$GN              = "$ARGV[1]";
$topLigandsFile  = "$ARGV[2]";
$SCORE           = "$ARGV[3]";


print "\n";

$folder = "top-$GN-$SCORE";

mkdir "top-$GN-$SCORE" || die "$!";

@dirs = glob "$dr*";

foreach $d (@dirs) 
{
  open(TOP, $topLigandsFile) || die "$!";

  while( <TOP> )
  {
    ($code, $score) = split;
    opendir(DIR, "$d/out-$GN") || die "$!";
    @files = readdir(DIR);
    foreach $f (@files)
    {
       if ($f =~ /$code/) {
         printf "  %30.30s ==> %s \n", $d, $f;
         $file = "$d/out-$GN/$f";
         copy("$file", "$folder") || die "$file can not be copied to top-$GN.\n";
         last;
       }
    }
    close DIR;
  }
}
