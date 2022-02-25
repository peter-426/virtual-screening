# dock.pl
# dock ligands with receptor
 
use Cwd;

sub runDock()
{
$d = getcwd;

$vina = "/common/home/a9913684/bin/vina";
$startingDir = "$d";

# $dockingN = "docking-test";  #  "$ARGV[0]";
# $GN       = "ftsZ";  # "$ARGV[1]";

$ligandFolder   = "$startingDir/ligand";
$receptorFolder = "$startingDir/receptor-$GN";
$outFolder      = "$startingDir/out-$GN";
$logFolder      = "$startingDir/log-$GN";

print "\nstarting dir = $startingDir \n";
print "ligandFolder   = $ligandFolder \n";
print "receptorFolder = $receptorFolder \n";
print "outFolder      = $outFolder \n";
print "logFolder      = $logFolder \n\n";


-d $receptorFolder || die "receptor folder does not exist\n"; 
-d $ligandFolder   || die "ligand folder does not exist\n"; 

mkdir $logFolder;
mkdir $outFolder;

chdir($ligandFolder) or die "Error: failed to chdir to $path $!";

opendir(DIR, $ligandFolder);
@files = sort ( grep /pdbqt$/, readdir(DIR) ); 
closedir(DIR);

$count = 0;

print "Docking ... \n";
$t = localtime();
print "$t\n";

$config   = "${receptorFolder}/conf.txt";
$receptor = "$receptorFolder/$GN.pdbqt";


-e $config   || die "config file does not exist\n"; 
-e $receptor || die "receptor file does not exist\n"; 

print "\n config=$config \n receptor=$receptor \n";


$max = $#files;

if( $start < 0 ) { $start = 0; }

if( $end > $max )  { $end = $max; }

if ($start > $end ) {  $start = $end;  }

@files = @files[$start..$end]; ##############################################################

foreach $file (@files) {
   
   if ($file =~ /.*pdbqt$/) 
   {
      print "$file \n";
      $count += 1;
      if ($count % 100 == 1) { print "$count >> $ligandFolder/$file\n";  }
	  
      $ligand   = "$ligandFolder/$file";
      $out      = "$outFolder/out_$file";
      $log      = "$logFolder/${file}_log.txt";
	  
      if ( ! -e $out ) {

        `$vina --config $config --ligand $ligand --receptor $receptor --out $out --log $log`  
      }
   }   
}

$t = localtime();
print "$t\n";

print "docked $count ligands\n";
}
1
