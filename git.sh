echo "Leave it blank if you don't want to give it and see the magic"
echo "Enter commit name: "
read commit

if [ -z "$commit" ];
then
echo "Are you noob?????, If you select noob then I will make commit name = 'lazy commit', press 1 or 2"
select yn in "Yes" "No"; do
    case $yn in
        Yes ) 
            git add .;
            git commit -m "lazy commit";
            git push; 
            break;;
        No ) 
            echo "Then give a name to your commit"
            exit;;
    esac
done
else
git add .
git commit -m "$commit"
git push
fi